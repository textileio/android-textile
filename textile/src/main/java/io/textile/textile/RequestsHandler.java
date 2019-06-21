package io.textile.textile;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import net.gotev.uploadservice.BinaryUploadRequest;
import net.gotev.uploadservice.HttpUploadRequest;
import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.util.Map;

import io.textile.pb.Model.CafeHTTPRequest;
import io.textile.pb.View.Strings;
import mobile.Callback;
import mobile.Mobile_;

import static net.gotev.uploadservice.Placeholders.*;

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
                node.writeCafeRequest(id, new Callback() {
                    @Override
                    public void call(byte[] req, Exception e) {
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
                    }
                });
            }
        } catch (Exception e) {
            Logger.error(getClass().getSimpleName(), e.getMessage());
        }
    }

    private String startUpload(final String id, final CafeHTTPRequest req) throws Exception {
        HttpUploadRequest request = new BinaryUploadRequest(applicationContext, id, req.getUrl())
                .setFileToUpload(req.getPath())
                .setMethod(req.getType().toString())
                .setMaxRetries(2);

        for (Map.Entry<String,String> entry : req.getHeadersMap().entrySet()) {
            request.addHeader(entry.getKey(), entry.getValue());
        }

        UploadNotificationConfig note = new UploadNotificationConfig();
        request.setNotificationConfig(getNotificationConfig(id));

        return request.startUpload();
    }

    private UploadNotificationConfig getNotificationConfig(final String uploadId) {
        UploadNotificationConfig config = new UploadNotificationConfig();

        PendingIntent clickIntent = PendingIntent.getActivity(applicationContext, 1, new Intent(
                applicationContext, UploadReceiverService.class), PendingIntent.FLAG_UPDATE_CURRENT);

        config.setTitleForAllStatuses("Textile")
                .setClickIntentForAllStatuses(clickIntent)
                .setClearOnActionForAllStatuses(true);

        config.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                + " at " + UPLOAD_RATE + " - " + PROGRESS;
//        config.getProgress().iconResourceID = R.drawable.ic_upload;
//        config.getProgress().iconColorResourceID = Color.BLUE;

        config.getCompleted().autoClear = true;
        config.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
//        config.getCompleted().iconResourceID = R.drawable.ic_upload_success;
//        config.getCompleted().iconColorResourceID = Color.GREEN;

        config.getError().message = "Error while uploading";
//        config.getError().iconResourceID = R.drawable.ic_upload_error;
//        config.getError().iconColorResourceID = Color.RED;

        config.getCancelled().message = "Upload has been cancelled";
//        config.getCancelled().iconResourceID = R.drawable.ic_cancelled;
//        config.getCancelled().iconColorResourceID = Color.YELLOW;

        return config;
    }
}
