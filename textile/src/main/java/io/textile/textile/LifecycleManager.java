package io.textile.textile;

import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

enum AppState {
    None, Background, Foreground
}

public class LifecycleManager extends Service implements LifecycleObserver {

    Textile textile;
    AppState appState;
    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        textile = Textile.instance();
        appState = AppState.None;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(this);
        stopNodeAndClearTimer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onForeground() {
        if (appState.equals(AppState.Foreground)) {
            return;
        }
        appState = AppState.Foreground;
        if (timer != null) {
            Textile.instance().notifyListenersOfCanceledPendingNodeStop();
            timer.cancel();
            timer = null;
        } else {
            Textile.instance().start();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onBackground() {
        if (appState.equals(AppState.None)) {
            appState = AppState.Background;
            if (timer != null) {
                stopNodeAfterDelay();
            } else {
                Textile.instance().start();
                stopNodeAfterDelay();
            }
        } else if (appState.equals(AppState.Foreground)) {
            appState = AppState.None;
            stopNodeAfterDelay();
        }
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
                stopNodeAndClearTimer();
            }
        }, delaySeconds * 1000);
    }

    private void stopNodeAndClearTimer() {
        timer = null;
        Textile.instance().stop();
    }
}
