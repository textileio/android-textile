package io.textile.textile;

import android.content.Context;
import android.content.Intent;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class RequestsBroadcastReceiver extends UploadServiceBroadcastReceiver {

    private static final String TAG = "RequestsBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.debug(TAG, "Received event");
        super.onReceive(context, intent);
    }

    @Override
    public void onProgress(Context context, UploadInfo info) {
        try {
            Textile.instance().cafes.updateCafeRequestProgress(
                    info.getUploadId(), info.getUploadedBytes(), info.getTotalBytes());
        } catch (final Exception e) {
            Logger.error(TAG, e.getMessage());
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
            Logger.error(TAG, e.getMessage());
        }
    }

    @Override
    public void onCompleted(Context context, UploadInfo info, ServerResponse response) {
        try {
            Textile.instance().cafes.completeCafeRequest(info.getUploadId());
        } catch (final Exception e) {
            Logger.error(TAG, e.getMessage());
        }
    }

    @Override
    public void onCancelled(Context context, UploadInfo info) {
        try {
            Textile.instance().cafes.failCafeRequest(info.getUploadId(), "Request cancelled");
        } catch (final Exception e) {
            Logger.error(TAG, e.getMessage());
        }
    }

    @Override
    public void register(final Context context) {
        super.register(context);
        Logger.info(TAG, "Registered");
    }

    @Override
    public void unregister(final Context context) {
        super.unregister(context);
        Logger.info(TAG, "Unregistered");
    }
}
