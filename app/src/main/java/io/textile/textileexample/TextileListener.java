package io.textile.textileexample;

import io.textile.textile.BaseTextileEventListener;

public class TextileListener extends BaseTextileEventListener {

    @Override
    public void nodeStarted() {
        super.nodeStarted();
        System.out.println("node started");
    }

    @Override
    public void nodeStopped() {
        super.nodeStopped();
        System.out.println("node stopped");
    }

    @Override
    public void nodeOnline() {
        super.nodeOnline();
        System.out.println("node online");
    }

    @Override
    public void willStopNodeInBackgroundAfterDelay(int seconds) {
        super.willStopNodeInBackgroundAfterDelay(seconds);
        System.out.println("will run in background for " + seconds + " seconds");
    }

    @Override
    public void canceledPendingNodeStop() {
        super.canceledPendingNodeStop();
        System.out.println("canceled pending stop");
    }
}
