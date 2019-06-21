package io.textile.textile;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class UploadReceiverService extends Service {

    Context applicationContext;

    class UploadReceiverBinder extends Binder {
        UploadReceiverService getService() {
            return UploadReceiverService.this;
        }
    }

    private UploadReceiverBinder binder = new UploadReceiverBinder();

    @Override
    public UploadReceiverBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastReceiver.register(getBaseContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastReceiver.unregister(getBaseContext());
    }

    private UploadServiceBroadcastReceiver broadcastReceiver = new UploadServiceBroadcastReceiver() {
        @Override
        public void onProgress(Context context, UploadInfo info) {
            try {
                Textile.instance().cafes.updateCafeRequestProgress(info.getUploadId(), info.getUploadedBytes(), info.getTotalBytes());
            } catch (final Exception e) {
                Logger.error(getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onError(Context context, UploadInfo info, ServerResponse response, Exception exception) {
            String message = "Request failed (";
            if (response != null) {
                message += "code=" + response.getHttpCode() + " ";
                message += "body=" + response.getBodyAsString() + " ";
            }

            if (exception != null) {
                message += "error=" + exception.getMessage();
            } else {
                message += "error=unknown";
            }
            message += ")";

            try {
                Textile.instance().cafes.failCafeRequest(info.getUploadId(), message);
            } catch (final Exception e) {
                Logger.error(getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onCompleted(Context context, UploadInfo info, ServerResponse response) {
            try {
                Textile.instance().cafes.completeCafeRequest(info.getUploadId());
            } catch (final Exception e) {
                Logger.error(getClass().getSimpleName(), e.getMessage());
            }
        }

        @Override
        public void onCancelled(Context context, UploadInfo info) {
            try {
                Textile.instance().cafes.failCafeRequest(info.getUploadId(), "Request cancelled");
            } catch (final Exception e) {
                Logger.error(getClass().getSimpleName(), e.getMessage());
            }
        }
    };
}
