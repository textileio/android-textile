package io.textile.textile;

import io.textile.pb.Model;
import io.textile.pb.Model.Peer;
import io.textile.pb.Model.Thread;
import mobile.Mobile_;

/**
 * Provides access to Textile profile related APIs
 */
public class Profile extends NodeDependent {

    Profile(final Mobile_ node) {
        super(node);
    }

    /**
     * Get the Peer object associated with the local Textile peer
     * @return The Peer object associated with the local Textile peer
     * @throws Exception The exception that occurred
     */
    public Peer get() throws Exception {
        final byte[] bytes = node.profile();
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
    public void setName(final String name) throws Exception {
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
     * Set the avatar
     * @param file The image file to use
     * @param handler An object that will get called with the resulting block
     */
    public void setAvatar(final String file, final Handlers.BlockHandler handler) {
        node.setAvatar(file, (data, e) -> {
            if (e != null) {
                handler.onError(e);
                return;
            }
            try {
                handler.onComplete(Model.Block.parseFrom(data));
            } catch (final Exception exception) {
                handler.onError(exception);
            }
        });
    }

    /**
     * Get the Textile account thread
     * @return The account thread
     * @throws Exception The exception that occurred
     */
    public Thread accountThread() throws Exception {
        final byte[] bytes = node.accountThread();
        return Thread.parseFrom(bytes);
    }
}
