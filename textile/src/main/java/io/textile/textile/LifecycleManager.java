package io.textile.textile;

import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Intent;
import android.os.IBinder;

public class LifecycleManager extends Service implements LifecycleObserver {

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("STARTING IT");
        return super.onStartCommand(intent, flags, startId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onForeground() {
        System.out.println("FOREGROUND");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onBackground() {
        System.out.println("BACKGROUND");
    }

}
