package io.textile.textile;

import io.textile.pb.Model.CafeSyncGroupStatus;
import io.textile.pb.Model.Notification;
import io.textile.pb.Model.Thread;
import io.textile.pb.Model.Contact;

/**
 * A default implementation of TextileEventListener that can be extended to override specific methods
 */
public abstract class BaseTextileEventListener implements TextileEventListener {

    @Override
    public void nodeStarted() {

    }

    @Override
    public void nodeFailedToStart(Exception e) {

    }

    @Override
    public void nodeStopped() {

    }

    @Override
    public void nodeFailedToStop(Exception e) {

    }

    @Override
    public void nodeOnline() {

    }

    @Override
    public void willStopNodeInBackgroundAfterDelay(int seconds) {

    }

    @Override
    public void canceledPendingNodeStop() {

    }

    @Override
    public void notificationReceived(Notification notification) {

    }

    @Override
    public void threadUpdateReceived(String threadId, FeedItemData feedItemData) {

    }

    @Override
    public void threadAdded(String threadId) {

    }

    @Override
    public void threadRemoved(String threadId) {

    }

    @Override
    public void accountPeerAdded(String peerId) {

    }

    @Override
    public void accountPeerRemoved(String peerId) {

    }

    @Override
    public void queryDone(String queryId) {

    }

    @Override
    public void queryError(String queryId, Exception e) {

    }

    @Override
    public void clientThreadQueryResult(String queryId, Thread thread) {

    }

    @Override
    public void contactQueryResult(String queryId, Contact contact) {

    }

    @Override
    public void syncUpdate(CafeSyncGroupStatus status) {

    }

    @Override
    public void syncComplete(CafeSyncGroupStatus status) {

    }

    @Override
    public void syncFailed(CafeSyncGroupStatus status) {

    }
}
