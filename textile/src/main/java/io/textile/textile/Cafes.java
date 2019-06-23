package io.textile.textile;

import io.textile.pb.Model.CafeSessionList;
import io.textile.pb.Model.CafeSession;
import mobile.Mobile_;

/**
 * Provides access to Textile cafes related APIs
 */
public class Cafes extends NodeDependent {

    Cafes(final Mobile_ node) {
        super(node);
    }

    /**
     * Used to register a remote Textile Cafe node with the local Textile node
     * @param host The peer id of the cafe being registered
     * @param token The API token for the cafe being registered
     * @param handler An object that will get called with the operation is complete
     */
    public void register(String peerId, String token, final Handlers.ErrorHandler handler) {
        node.registerCafe(peerId, token, (e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete();
            } catch (Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Used to deregister a previously registered Textile Cafe
     * @param peerId The peer id of the cafe you want to deregister
     * @param handler An object that will get called with the operation is complete
     */
    public void deregister(String peerId, final Handlers.ErrorHandler handler) {
        node.deregisterCafe(peerId, (e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete();
            } catch (Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Used to refresh an individual Textile Cafe session
     * @param peerId The peer id of the cafe who's session you want to refresh
     * @param handler An object that will get called with the result of the operation
     */
    public void refreshSession(String peerId, final Handlers.CafeSessionHandler handler) {
        node.refreshCafeSession(peerId, (data, e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            if (data == null) {
                handler.onComplete(null);
                return;
            }
            try {
                handler.onComplete(CafeSession.parseFrom(data));
            } catch (Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Triggers the async process of checking for messages from all registered cafes
     * @throws Exception The exception that occurred
     */
    public void checkMessages() throws Exception {
        node.checkCafeMessages();
    }

    /**
     * Fetches the CafeSession object for a previously registered Textile Cafe node
     * @param peerId The peer id of the previously registered cafe node
     * @return The CafeSession for the previously registered cafe
     * @throws Exception The exception that occurred
     */
    public CafeSession session(String peerId) throws Exception {
        /*
         * cafeSession returns null if no session is found.
         * Be sure we return null in that case and not a default CafeSession
         */
        byte[] bytes = node.cafeSession(peerId);
        return bytes != null ? CafeSession.parseFrom(bytes) : null;
    }

    /**
     * Used to get sessions for all previously registered Textile Cafes
     * @return An object containing a list of cafe sessions
     * @throws Exception The exception that occurred
     */
    public CafeSessionList sessions() throws Exception {
        byte[] bytes = node.cafeSessions();
        return CafeSessionList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    byte[] cafeRequests(long limit) throws Exception {
        return node.cafeRequests(limit);
    }

    void cafeRequestPending(String id) throws Exception {
        node.cafeRequestPending(id);
    }

    void cafeRequestNotPending(String id) throws Exception {
        node.cafeRequestNotPending(id);
    }

    void completeCafeRequest(String id) throws Exception {
        node.completeCafeRequest(id);
    }

    void failCafeRequest(String id, String reason) throws Exception {
        node.failCafeRequest(id, reason);
    }

    void updateCafeRequestProgress(String id, long transferred, long total) throws Exception {
        node.updateCafeRequestProgress(id, transferred, total);
    }
}
