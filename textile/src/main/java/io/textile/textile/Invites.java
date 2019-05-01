package io.textile.textile;

import io.textile.pb.View.InviteViewList;
import io.textile.pb.View.ExternalInvite;
import mobile.Mobile_;

public class Invites extends NodeDependent {

    Invites(Mobile_ node) {
        super(node);
    }

    public void add(String threadId, String address) throws Exception {
        node.addInvite(threadId, address);
    }

    public ExternalInvite addExternal(String threadId) throws Exception {
        byte[] bytes = node.addExternalInvite(threadId);
        return bytes != null ? ExternalInvite.parseFrom(bytes) : null;
    }

    public InviteViewList list() throws Exception {
        byte[] bytes = node.invites();
        return InviteViewList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public String accept(String inviteId) throws Exception {
        return node.acceptInvite(inviteId);
    }

    public String acceptExternal(String inviteId, String key) throws Exception {
        return node.acceptExternalInvite(inviteId, key);
    }

    public void ignore(String inviteId) throws Exception {
        node.ignoreInvite(inviteId);
    }
}
