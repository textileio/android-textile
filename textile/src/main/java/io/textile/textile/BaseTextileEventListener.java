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
    public void nodeFailedToStart(final Exception e) {

    }

    @Override
    public void nodeStopped() {

    }

    @Override
    public void nodeFailedToStop(final Exception e) {

    }

    @Override
    public void nodeOnline() {

    }

    @Override
    public void willStopNodeInBackgroundAfterDelay(final int seconds) {

    }

    @Override
    public void canceledPendingNodeStop() {

    }

    @Override
    public void notificationReceived(final Notification notification) {

    }

    @Override
    public void threadUpdateReceived(final String threadId, final FeedItemData feedItemData) {

    }

    @Override
    public void threadAdded(final String threadId) {

    }

    @Override
    public void threadRemoved(final String threadId) {

    }

    @Override
    public void accountPeerAdded(final String peerId) {

    }

    @Override
    public void accountPeerRemoved(final String peerId) {

    }

    @Override
    public void queryDone(final String queryId) {

    }

    @Override
    public void queryError(final String queryId, final Exception e) {

    }

    @Override
    public void clientThreadQueryResult(final String queryId, final Thread thread) {

    }

    @Override
    public void contactQueryResult(final String queryId, final Contact contact) {

    }

    @Override
    public void syncUpdate(final CafeSyncGroupStatus status) {

    }

    @Override
    public void syncComplete(final CafeSyncGroupStatus status) {

    }

    @Override
    public void syncFailed(final CafeSyncGroupStatus status) {

    }
}
