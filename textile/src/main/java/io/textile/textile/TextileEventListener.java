package io.textile.textile;

import io.textile.pb.Model.Contact;
import io.textile.pb.Model.CafeClientThread;
import io.textile.pb.Model.Notification;
import io.textile.pb.View.FeedItem;

public interface TextileEventListener {

    void nodeStarted();

    void nodeFailedToStart(Exception e);

    void nodeStopped();

    void nodeFailedToStop(Exception e);

    void nodeOnline();

    void notificationReceived(Notification notification);

    void threadUpdateReceived(FeedItem feedItem);

    void threadAdded(String threadId);

    void threadRemoved(String threadId);

    void accountPeerAdded(String peerId);

    void accountPeerRemoved(String peerId);

    void queryDone(String queryId);

    void queryError(String queryId, Exception e);

    void clientThreadQueryResult(String queryId, CafeClientThread clientThread);

    void contactQueryResult(String queryId, Contact contact);
}
