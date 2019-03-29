package io.textile.textile;

import io.textile.pb.Model.CafeSessionList;
import io.textile.pb.Model.CafeSession;
import mobile.Mobile_;

public class Cafes extends NodeDependent {

    Cafes(Mobile_ node) {
        super(node);
    }

    public void register(String host, String token) throws Exception {
        this.node.registerCafe(host, token);
    }

    public CafeSession session(String peerId) throws Exception {
        byte[] bytes = this.node.cafeSession(peerId);
        return CafeSession.parseFrom(bytes);
    }

    public CafeSessionList sessions() throws Exception {
        byte[] bytes = this.node.cafeSessions();
        return CafeSessionList.parseFrom(bytes);
    }

    public CafeSession refreshSession(String peerId) throws Exception {
        byte[] bytes = this.node.refreshCafeSession(peerId);
        return CafeSession.parseFrom(bytes);
    }

    public void deregister(String peerId) throws Exception {
        this.node.deregisterCafe(peerId);
    }

    public void checkMessages() throws Exception {
        this.node.checkCafeMessages();
    }
}
