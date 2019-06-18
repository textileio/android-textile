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
     * @throws Exception The exception that occurred
     */
    public void register(String peerId, String token) throws Exception {
        node.registerCafe(peerId, token);
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

    /**
     * Used to refresh an individual Textile Cafe session
     * @param peerId The peer id of the cafe who's session you want to refresh
     * @return The refreshed cafe session
     * @throws Exception The exception that occurred
     */
    public CafeSession refreshSession(String peerId) throws Exception {
        /*
         * refreshCafeSession returns null if no session is found.
         * Be sure we return null in that case and not a default CafeSession
         */
        byte[] bytes = node.refreshCafeSession(peerId);
        return bytes != null ? CafeSession.parseFrom(bytes) : null;
    }

    /**
     * Used to deregister a previously registered Textile Cafe
     * @param peerId The peer id of the cafe you want to deregister
     * @throws Exception The exception that occurred
     */
    public void deregister(String peerId) throws Exception {
        node.deregisterCafe(peerId);
    }

    /**
     * Triggers the async process of checking for messages from all registered cafes
     * @throws Exception The exception that occurred
     */
    public void checkMessages() throws Exception {
        node.checkCafeMessages();
    }
}
