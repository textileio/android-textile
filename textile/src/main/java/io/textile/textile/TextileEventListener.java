package io.textile.textile;

import io.textile.pb.Model.Contact;
import io.textile.pb.Model.Thread;
import io.textile.pb.Model.Notification;
import io.textile.pb.View.FeedItem;

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
    void nodeFailedToStart(Exception e);

    /**
     * Called when the Textile node is successfully stopped
     */
    void nodeStopped();

    /**
     * Called when the Textile node fails to stop
     * @param e The error describing the failure
     */
    void nodeFailedToStop(Exception e);

    /**
     * Called when the Textile node comes online
     */
    void nodeOnline();

    /**
     * Called when the node is scheduled to be stopped in the future
     * @param seconds The amount of time the node will run for before being stopped
     */
    void willStopNodeInBackgroundAfterDelay(int seconds);

    /**
     * Called when the scheduled node stop is cancelled, the node will continue running
     */
    void canceledPendingNodeStop();

    /**
     * Called when the Textile node receives a notification
     * @param notification The received notification
     */
    void notificationReceived(Notification notification);

    /**
     * Called when any thread receives an update
     * @param feedItem The thread update
     */
    void threadUpdateReceived(FeedItem feedItem);

    /**
     * Called when a new thread is successfully added
     * @param threadId The id of the newly added thread
     */
    void threadAdded(String threadId);

    /**
     * Called when a thread is successfully removed
     * @param threadId The id of the removed thread
     */
    void threadRemoved(String threadId);

    /**
     * Called when a peer node is added to the user account
     * @param peerId The id of the new account peer
     */
    void accountPeerAdded(String peerId);

    /**
     * Called when an account peer is removed from the user account
     * @param peerId The id of the removed account peer
     */
    void accountPeerRemoved(String peerId);

    /**
     * Called when any query is complete
     * @param queryId The id of the completed query
     */
    void queryDone(String queryId);

    /**
     * Called when any query fails
     * @param queryId The id of the failed query
     * @param error The error describing the failure
     */
    void queryError(String queryId, Exception e);

    /**
     * Called when there is a thread query result available
     * @param queryId The id of the corresponding query
     * @param thread A thread query result
     */
    void clientThreadQueryResult(String queryId, Thread thread);

    /**
     * Called when there is a contact query result available
     * @param queryId The id of the corresponding query
     * @param contact A contact query result
     */
    void contactQueryResult(String queryId, Contact contact);
}
