package io.textile.textile;

import android.content.Context;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.HttpUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.Map;

import io.textile.pb.Model.CafeHTTPRequest;
import io.textile.pb.View.Strings;
import mobile.Callback;
import mobile.Mobile_;

/**
 * Defines a mockable subset of Mobile_ used by RequestsHandler
 */
interface Requests {
    byte[] cafeRequests(long limit) throws Exception;
    void cafeRequestPending(String id) throws Exception;
    void completeCafeRequest(String id) throws Exception;
    void failCafeRequest(String id, String reason) throws Exception;
    void writeCafeRequest(String var1, Callback var2);
}

/**
 * Implements a subset of Mobile_ used by RequestsHandler
 */
class MobileRequests extends NodeDependent implements Requests {
    MobileRequests(final Mobile_ node) {
        super(node);
    }

    public byte[] cafeRequests(long limit) throws Exception {
        return node.cafeRequests(limit);
    }

    public void cafeRequestPending(String id) throws Exception {
        node.cafeRequestPending(id);
    }

    public void completeCafeRequest(String id) throws Exception {
        node.completeCafeRequest(id);
    }

    public void failCafeRequest(String id, String reason) throws Exception {
        node.failCafeRequest(id, reason);
    }

    public void writeCafeRequest(String id, Callback cb) {
        node.writeCafeRequest(id, cb);
    }
}

/**
 * Handles HTTP requests queued by the Textile node
 */
class RequestsHandler {
    private Requests api;
    private Context applicationContext;
    private int batchSize;
    private boolean flushing;

    RequestsHandler(final Requests api, final Context applicationContext, final int batchSize) {
        this.api = api;
        this.applicationContext = applicationContext;
        this.batchSize = batchSize;
        this.flushing = false;
    }

    void flush() {
        if (flushing) {
            return;
        }
        flushing = true;

        try {
            // 1. List a batch of request ids
            final Strings ids = Strings.parseFrom(api.cafeRequests(batchSize));

            // 2. Mark each as pending so additional calls to flush do not yield the same requests
            for (final String id : ids.getValuesList()) {
                api.cafeRequestPending(id);
            }

            // 2. Write each request to disk
            for (final String id : ids.getValuesList()) {
                api.writeCafeRequest(id, new Callback() {
                    @Override
                    public void call(byte[] bytes, Exception e) {
                        if (e != null) {
                            // @todo Unmark as pending (new method needed)
                            return;
                        }
                        // 4. Start the request
                        try {
                            String uploadId = startUpload(id, CafeHTTPRequest.parseFrom(bytes));
                        } catch (Exception exception) {
                            // @todo Unmark as pending (new method needed)
                        }
                    }
                });
            }
        } catch (Exception e) {
            flushing = false;
        }

        flushing = false;
    }

    private String startUpload(final String id, final CafeHTTPRequest req) throws Exception {
        UploadStatusDelegate statusDelegate = new UploadStatusDelegate() {
            @Override
            public void onProgress(Context context, UploadInfo uploadInfo) {
                // @todo emit progress events
            }

            @Override
            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                String message = "Request failed (";
                if (serverResponse != null) {
                    message += "code=" + serverResponse.getHttpCode() + " ";
                    message += "body=" + serverResponse.getBodyAsString() + " ";
                }

                if (exception != null) {
                    message += "error=" + exception.getMessage();
                } else {
                    message += "error=unknown";
                }
                message += ")";

                try {
                    api.failCafeRequest(id, message);
                } catch (final Exception e) {
                    // noop
                }
            }

            @Override
            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                try {
                    api.completeCafeRequest(id);
                } catch (final Exception e) {
                    // noop
                }
            }

            @Override
            public void onCancelled(Context context, UploadInfo uploadInfo) {
                try {
                    api.failCafeRequest(id, "Request cancelled");
                } catch (final Exception e) {
                    // noop
                }
            }
        };

        HttpUploadRequest request = new BinaryUploadRequest(applicationContext, id, req.getUrl())
                .setFileToUpload(req.getPath())
                .setMethod(req.getType().toString())
                .setMaxRetries(2)
                .setDelegate(statusDelegate);

        for (Map.Entry<String,String> entry : req.getHeadersMap().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        return request.startUpload();
    }
}
