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
    public LifecycleBinder onBind(Intent intent) {
        return binder;
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
        int delaySeconds = 60;
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
        Textile.instance().stop();
        nodeStoppedListener.onNodeStopped();
        stopSelf();
    }

    void cancelPendingNodeStop() {
        Textile.instance().notifyListenersOfCanceledPendingNodeStop();
        timer.cancel();
        timer = null;
    }
}
