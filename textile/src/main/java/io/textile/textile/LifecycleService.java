package io.textile.textile;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;

import java.util.Timer;
import java.util.TimerTask;

public class LifecycleService extends Service {

    interface NodeStoppedListener {
        void onNodeStopped();
    }

    class LifecycleBinder extends Binder {
        LifecycleService getService() {
            return LifecycleService.this;
        }
    }

    private LifecycleBinder binder = new LifecycleBinder();
    private Timer timer;

    NodeStoppedListener nodeStoppedListener;

    @Override
    public LifecycleBinder onBind(final Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopNodeImmediately();
    }

    void startNode() {
        Textile.instance().start();
    }

    void stopNodeAfterDelay() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        final int delaySeconds = 60;
        Textile.instance().notifyListenersOfPendingNodeStop(delaySeconds);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stopNodeImmediately();
            }
        }, delaySeconds * 1000);
    }

    void stopNodeImmediately() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        Textile.instance().stop(new Handlers.ErrorHandler() {
            @Override
            public void onComplete() {
                // Sometimes this function is run while nodeStoppedListener is still null, crashing the app
                if (nodeStoppedListener != null) {
                    nodeStoppedListener.onNodeStopped();
                }
                stopSelf();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    void cancelPendingNodeStop() {
        Textile.instance().notifyListenersOfCanceledPendingNodeStop();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
