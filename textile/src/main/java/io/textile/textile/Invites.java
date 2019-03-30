package io.textile.textile;

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
        return ExternalInvite.parseFrom(bytes);
    }

    public String acceptExternal(String inviteId, String key) throws Exception {
        return node.acceptExternalInvite(inviteId, key);
    }
}
