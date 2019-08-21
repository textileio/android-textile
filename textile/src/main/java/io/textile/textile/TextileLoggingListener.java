package io.textile.textile;

import io.textile.pb.Model.CafeSyncGroupStatus;

public class TextileLoggingListener extends BaseTextileEventListener {

    @Override
    public void nodeStarted() {
        super.nodeStarted();
        System.out.println("------------> node started");
    }

    @Override
    public void nodeStopped() {
        super.nodeStopped();
        System.out.println("------------> node stopped");
    }

    @Override
    public void nodeOnline() {
        super.nodeOnline();
        System.out.println("------------> node online");
    }

    @Override
    public void threadUpdateReceived(final String threadId, final FeedItemData feedItemData) {
        super.threadUpdateReceived(threadId, feedItemData);
        System.out.println("------------> thread update received: " + threadId);
    }

    @Override
    public void threadAdded(final String threadId) {
        super.threadAdded(threadId);
        System.out.println("------------> thread added: " + threadId);
    }

    @Override
    public void threadRemoved(final String threadId) {
        super.threadRemoved(threadId);
        System.out.println("------------> thread removed: " + threadId);
    }

    @Override
    public void syncUpdate(final CafeSyncGroupStatus status) {
        super.syncUpdate(status);
        int progress;
        if (status.getGroupsSizeTotal() > 0) {
            progress = (int) (status.getGroupsSizeComplete() * 100f / status.getGroupsSizeTotal());
        } else {
            progress = 0;
        }
        System.out.println("------------> sync update " + status.getId() + ": " + progress + "%");
    }

    @Override
    public void syncComplete(final CafeSyncGroupStatus status) {
        super.syncComplete(status);
        System.out.println("------------> sync complete: " + status.getId());
    }

    @Override
    public void syncFailed(final CafeSyncGroupStatus status) {
        super.syncFailed(status);
        System.out.println("------------> sync failed: " + status.getId());
    }
}
