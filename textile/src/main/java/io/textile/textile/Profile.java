package io.textile.textile;

import io.textile.pb.Model.Peer;
import io.textile.pb.Model.Thread;
import mobile.Mobile_;

/**
 * Provides access to Textile profile related APIs
 */
public class Profile extends NodeDependent {

    Profile(Mobile_ node) {
        super(node);
    }

    /**
     * Get the Peer object associated with the local Textile peer
     * @return The Peer object associated with the local Textile peer
     * @throws Exception The exception that occurred
     */
    public Peer get() throws Exception {
        byte[] bytes = node.profile();
        return Peer.parseFrom(bytes);
    }

    /**
     * Get the user name associated with the local Textile peer
     * @return The user name associated with the local Textile peer
     * @throws Exception The exception that occurred
     */
    public String name() throws Exception {
        return node.name();
    }

    /**
     * Set the user name
     * @param name The new user name
     * @throws Exception The exception that occurred
     */
    public void setName(String name) throws Exception {
        node.setName(name);
    }

    /**
     * Get the target of the profile avatar
     * @return The target of the avatar
     * @throws Exception The exception that occurred
     */
    public String avatar() throws Exception {
        return node.avatar();
    }

    /**
     * Get the Textile account thread
     * @return The account thread
     * @throws Exception The exception that occurred
     */
    public Thread accountThread() throws Exception {
        byte[] bytes = node.accountThread();
        return Thread.parseFrom(bytes);
    }
}
