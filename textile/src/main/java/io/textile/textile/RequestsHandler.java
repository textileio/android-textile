package io.textile.textile;

import android.content.Context;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.HttpUploadRequest;
import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.Map;

import io.textile.pb.Model.CafeHTTPRequest;
import io.textile.pb.View.Strings;
import mobile.Mobile_;

/**
 * Handles HTTP requests queued by the Textile node
 */
class RequestsHandler {

    private Mobile_ node;
    private Context applicationContext;
    private int batchSize;

    RequestsHandler(final Mobile_ node, final Context applicationContext, final int batchSize) {
        this.node = node;
        this.applicationContext = applicationContext;
        this.batchSize = batchSize;
    }

    void flush() {
        try {
            // 1. List a batch of request ids
            byte[] result = node.cafeRequests(batchSize);
            if (result == null) {
                return;
            }
            final Strings ids = Strings.parseFrom(result);

            // 2. Mark each as pending so additional calls to flush do not yield the same requests
            for (final String id : ids.getValuesList()) {
                node.cafeRequestPending(id);
            }

            // 3. Write each request to disk
            for (final String id : ids.getValuesList()) {
                node.writeCafeRequest(id, (req, e) -> {
                    if (e != null) {
                        try {
                            node.cafeRequestNotPending(id);
                        } catch (Exception ee) {
                            Logger.error(getClass().getSimpleName(), ee.getMessage());
                        }
                        return;
                    }
                    // 4. Start the request
                    try {
                        String uploadId = startUpload(id, CafeHTTPRequest.parseFrom(req));
                    } catch (Exception ee) {
                        try {
                            node.cafeRequestNotPending(id);
                        } catch (Exception eee) {
                            Logger.error(getClass().getSimpleName(), eee.getMessage());
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.error(getClass().getSimpleName(), e.getMessage());
        }
    }

    private String startUpload(final String id, final CafeHTTPRequest req) throws Exception {
        UploadNotificationConfig config = new UploadNotificationConfig();
        config.setTitleForAllStatuses("Sync");
        config.setRingToneEnabled(false);
        config.getCompleted().autoClear = true;
        config.getProgress().autoClear = true;
        config.getCancelled().autoClear = true;
        config.getError().autoClear = true;

        HttpUploadRequest request = new BinaryUploadRequest(applicationContext, id, req.getUrl())
                .setFileToUpload(req.getPath())
                .setMethod(req.getType().toString())
                .setMaxRetries(2)
                .setNotificationConfig(config)
                .setDelegate(null);

        for (Map.Entry<String,String> entry : req.getHeadersMap().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        return request.startUpload();
    }
}
