package io.textile.textile;

import io.textile.pb.View.InviteViewList;
import io.textile.pb.View.ExternalInvite;
import mobile.Mobile_;

/**
 * Provides access to Textile invites related APIs
 */
public class Invites extends NodeDependent {

    Invites(final Mobile_ node) {
        super(node);
    }

    /**
     * Invite a contact to a thread
     * @param threadId The id of the thread to invite to
     * @param address The address of the contact to invite
     * @throws Exception The exception that occurred
     */
    public void add(final String threadId, final String address) throws Exception {
        node.addInvite(threadId, address);
    }

    /**
     * Create an external invite, one for someone who isn't in Textile yet
     * @param threadId The id of the thread to invite to
     * @return An object representing the external invite
     * @throws Exception The exception that occurred
     */
    public ExternalInvite addExternal(final String threadId) throws Exception {
        final byte[] bytes = node.addExternalInvite(threadId);
        return ExternalInvite.parseFrom(bytes);
    }

    /**
     * View a list of pending incoming invites
     * @return An object containing a list of pending invites
     * @throws Exception The exception that occurred
     */
    public InviteViewList list() throws Exception {
        final byte[] bytes = node.invites();
        return InviteViewList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * Accept an invite
     * @param inviteId The id of the invite to accept
     * @return The hash of the new thread join block
     * @throws Exception The exception that occurred
     */
    public String accept(final String inviteId) throws Exception {
        return node.acceptInvite(inviteId);
    }

    /**
     * Accept an external invite
     * @param inviteId The id of the external invite to accept
     * @param key The key associated with the external invite to accept
     * @return The hash of the new thread join block
     * @throws Exception The exception that occurred
     */
    public String acceptExternal(final String inviteId, final String key) throws Exception {
        return node.acceptExternalInvite(inviteId, key);
    }

    /**
     * Ignore an invite
     * @param inviteId The id of the invite to ignore
     * @throws Exception The exception that occurred
     */
    public void ignore(final String inviteId) throws Exception {
        node.ignoreInvite(inviteId);
    }
}
