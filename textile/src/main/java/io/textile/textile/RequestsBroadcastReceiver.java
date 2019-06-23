package io.textile.textile;

import android.content.Context;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class RequestsBroadcastReceiver extends UploadServiceBroadcastReceiver {

    @Override
    public void onProgress(Context context, UploadInfo info) {
        super.onProgress(context, info);

        try {
            Textile.instance().cafes.updateCafeRequestProgress(info.getUploadId(), info.getUploadedBytes(), info.getTotalBytes());
        } catch (final Exception e) {
            Logger.error(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onError(Context context, UploadInfo info, ServerResponse response, Exception exception) {
        super.onError(context, info, response, exception);

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
        super.onCompleted(context, info, response);

        try {
            Textile.instance().cafes.completeCafeRequest(info.getUploadId());
        } catch (final Exception e) {
            Logger.error(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onCancelled(Context context, UploadInfo info) {
        super.onCancelled(context, info);

        try {
            Textile.instance().cafes.failCafeRequest(info.getUploadId(), "Request cancelled");
        } catch (final Exception e) {
            Logger.error(getClass().getSimpleName(), e.getMessage());
        }
    }
}
