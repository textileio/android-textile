package io.textile.textile;

import io.textile.pb.Model.CafeSyncGroupStatus;
import io.textile.pb.Model.Contact;
import io.textile.pb.Model.Thread;
import io.textile.pb.Model.Notification;

/**
 * Interface that can be implemented in order to receive callbacks from Textile about events of interest
 */
public interface TextileEventListener {

    /**
     * Called when the Textile node is started successfully
     */
    void nodeStarted();

    /**
     * Called when the Textile node fails to start
     * @param e The error describing the failure
     */
    void nodeFailedToStart(final Exception e);

    /**
     * Called when the Textile node is successfully stopped
     */
    void nodeStopped();

    /**
     * Called when the Textile node fails to stop
     * @param e The error describing the failure
     */
    void nodeFailedToStop(final Exception e);

    /**
     * Called when the Textile node comes online
     */
    void nodeOnline();

    /**
     * Called when the node is scheduled to be stopped in the future
     * @param seconds The amount of time the node will run for before being stopped
     */
    void willStopNodeInBackgroundAfterDelay(final int seconds);

    /**
     * Called when the scheduled node stop is cancelled, the node will continue running
     */
    void canceledPendingNodeStop();

    /**
     * Called when the Textile node receives a notification
     * @param notification The received notification
     */
    void notificationReceived(final Notification notification);

    /**
     * Called when any thread receives an update
     * @param threadId The id of the thread being updated
     * @param feedItemData The thread update
     */
    void threadUpdateReceived(final String threadId, final FeedItemData feedItemData);

    /**
     * Called when a new thread is successfully added
     * @param threadId The id of the newly added thread
     */
    void threadAdded(final String threadId);

    /**
     * Called when a thread is successfully removed
     * @param threadId The id of the removed thread
     */
    void threadRemoved(final String threadId);

    /**
     * Called when a peer node is added to the user account
     * @param peerId The id of the new account peer
     */
    void accountPeerAdded(final String peerId);

    /**
     * Called when an account peer is removed from the user account
     * @param peerId The id of the removed account peer
     */
    void accountPeerRemoved(final String peerId);

    /**
     * Called when any query is complete
     * @param queryId The id of the completed query
     */
    void queryDone(final String queryId);

    /**
     * Called when any query fails
     * @param queryId The id of the failed query
     * @param e The error describing the failure
     */
    void queryError(final String queryId, final Exception e);

    /**
     * Called when there is a thread query result available
     * @param queryId The id of the corresponding query
     * @param thread A thread query result
     */
    void clientThreadQueryResult(final String queryId, final Thread thread);

    /**
     * Called when there is a contact query result available
     * @param queryId The id of the corresponding query
     * @param contact A contact query result
     */
    void contactQueryResult(final String queryId, final Contact contact);

    /**
     * Called when there is an update about a sync group
     * @param status An object describing the sync status
     */
    void syncUpdate(final CafeSyncGroupStatus status);

    /**
     * Called when a sync group is complete
     * @param status An object describing the sync status
     */
    void syncComplete(final CafeSyncGroupStatus status);

    /**
     * Called when a sync group has failed
     * @param status An object describing the sync status
     */
    void syncFailed(final CafeSyncGroupStatus status);
}
