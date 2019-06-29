package io.textile.textile;

import android.content.Context;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RequestsBroadcastReceiver extends UploadServiceBroadcastReceiver {

    private static final String TAG = "RequestsBroadcastReceiver";

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onProgress(final Context context, final UploadInfo info) {
        handle(() -> Textile.instance().cafes.updateCafeRequestProgress(
                info.getUploadId(), info.getUploadedBytes(), info.getTotalBytes()));
    }

    @Override
    public void onError(
            final Context context, final UploadInfo info, final ServerResponse response,
            final Exception exception) {
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
        final String fmessage = message;

        handle(() -> Textile.instance().cafes.failCafeRequest(info.getUploadId(), fmessage));
    }

    @Override
    public void onCompleted(final Context context, final UploadInfo info, final ServerResponse response) {
        handle(() -> Textile.instance().cafes.completeCafeRequest(info.getUploadId()));
        handle(this::flushNext);
    }

    @Override
    public void onCancelled(final Context context, final UploadInfo info) {
        handle(() -> Textile.instance().cafes.failCafeRequest(
                info.getUploadId(), "Request cancelled"));
        handle(this::flushNext);
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

    private void handle(final Callable<Void> callable) {
        final Future<Void> future = executor.submit(callable);
        try {
            future.get();
        } catch (final Exception e) {
            Logger.error(TAG, e.getMessage());
        }
    }

    private Void flushNext() {
        if (!UploadService.getTaskList().isEmpty()) {
            Textile.instance().requestsHandler.flush();
        }
        return null;
    }
}
