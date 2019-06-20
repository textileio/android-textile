package io.textile.textile;

import io.textile.pb.Model.CafeSyncGroupStatus;

public class TestTextileListener extends BaseTextileEventListener {

    @Override
    public void nodeStarted() {
        super.nodeStarted();
        System.out.println(":::::> node started");
    }

    @Override
    public void nodeStopped() {
        super.nodeStopped();
        System.out.println(":::::> node stopped");
    }

    @Override
    public void nodeOnline() {
        super.nodeOnline();
        System.out.println(":::::> node online");
    }

    @Override
    public void threadAdded(String threadId) {
        super.nodeStarted();
        System.out.println(":::::> thread added: " + threadId);
    }

    @Override
    public void threadRemoved(String threadId) {
        super.nodeStarted();
        System.out.println(":::::> thread removed: " + threadId);
    }

    @Override
    public void syncUpdate(CafeSyncGroupStatus status) {
        super.nodeStarted();
        long progress;
        System.out.println(":::::> sync update: ");
        if (status.getGroupsSizeTotal() > 0) {
            progress = status.getGroupsSizeComplete() / status.getGroupsSizeTotal();
        } else {
            progress = 0;
        }
        System.out.println(":::::> sync update " + status.getId() + ": " + progress*100 + "%");
    }

    @Override
    public void syncComplete(CafeSyncGroupStatus status) {
        super.nodeStarted();
        System.out.println(":::::> sync complete: " + status.getId());
    }

    @Override
    public void syncFailed(CafeSyncGroupStatus status) {
        super.nodeStarted();
        System.out.println(":::::> sync failed: " + status.getId());
    }
}
