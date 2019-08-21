package io.textile.textile;

import io.textile.pb.Model.CafeSessionList;
import io.textile.pb.Model.CafeSession;
import mobile.Mobile_;
import mobile.ProtoCallback;

/**
 * Provides access to Textile cafes related APIs
 */
public class Cafes extends NodeDependent {

    Cafes(final Mobile_ node) {
        super(node);
    }

    /**
     * Used to register a remote Textile Cafe node with the local Textile node
     * @param peerId The peer id of the cafe being registered
     * @param token The API token for the cafe being registered
     * @param handler An object that will get called with the operation is complete
     */
    public void register(final String peerId, final String token, final Handlers.ErrorHandler handler) {
        node.registerCafe(peerId, token, (e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete();
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Used to deregister a previously registered Textile Cafe
     * @param peerId The peer id of the cafe you want to deregister
     * @param handler An object that will get called with the operation is complete
     */
    public void deregister(final String peerId, final Handlers.ErrorHandler handler) {
        node.deregisterCafe(peerId, (e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete();
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Used to refresh an individual Textile Cafe session
     * @param peerId The peer id of the cafe who's session you want to refresh
     * @param handler An object that will get called with the result of the operation
     */
    public void refreshSession(final String peerId, final Handlers.CafeSessionHandler handler) {
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
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Checks messages from all registered cafes
     * @param handler An object that will get called with the operation is complete
     */
    public void checkMessages(final Handlers.ErrorHandler handler) {
        node.checkCafeMessages((e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete();
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Fetches the CafeSession object for a previously registered Textile Cafe node
     * @param peerId The peer id of the previously registered cafe node
     * @return The CafeSession for the previously registered cafe
     * @throws Exception The exception that occurred
     */
    public CafeSession session(final String peerId) throws Exception {
        /*
         * cafeSession returns null if no session is found.
         * Be sure we return null in that case and not a default CafeSession
         */
        final byte[] bytes = node.cafeSession(peerId);
        return bytes != null ? CafeSession.parseFrom(bytes) : null;
    }

    /**
     * Used to get sessions for all previously registered Textile Cafes
     * @return An object containing a list of cafe sessions
     * @throws Exception The exception that occurred
     */
    public CafeSessionList sessions() throws Exception {
        final byte[] bytes = node.cafeSessions();
        return CafeSessionList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    byte[] cafeRequests(final long limit) throws Exception {
        return node.cafeRequests(limit);
    }

    void cafeRequestPending(final String id) throws Exception {
        node.cafeRequestPending(id);
    }

    void cafeRequestNotPending(final String id) throws Exception {
        node.cafeRequestNotPending(id);
    }

    Void completeCafeRequest(final String id) throws Exception {
        node.completeCafeRequest(id);
        return null;
    }

    Void failCafeRequest(final String id, final String reason) throws Exception {
        node.failCafeRequest(id, reason);
        return null;
    }

    Void updateCafeRequestProgress(final String id, final long transferred, final long total) throws Exception {
        node.updateCafeRequestProgress(id, transferred, total);
        return null;
    }

    void writeCafeRequest(final String id, final ProtoCallback cb) {
        node.writeCafeRequest(id, cb);
    }
}
