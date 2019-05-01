package io.textile.textile;

import io.textile.pb.Model.Peer;
import mobile.Mobile_;

public class Profile extends NodeDependent {

    Profile(Mobile_ node) {
        super(node);
    }

    public Peer get() throws Exception {
        byte[] bytes = node.profile();
        return Peer.parseFrom(bytes);
    }

    public String name() throws Exception {
        return node.name();
    }

    public void setName(String name) throws Exception {
        node.setName(name);
    }

    public String avatar() throws Exception {
        return node.avatar();
    }

    public void setAvatar(String hash) throws Exception {
        node.setAvatar(hash);
    }
}
