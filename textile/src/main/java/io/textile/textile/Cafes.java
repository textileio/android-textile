package io.textile.textile;

import io.textile.pb.Model.CafeSessionList;
import io.textile.pb.Model.CafeSession;
import mobile.Mobile_;

public class Cafes extends NodeDependent {

    Cafes(Mobile_ node) {
        super(node);
    }

    public void register(String host, String token) throws Exception {
        node.registerCafe(host, token);
    }

    public CafeSession session(String peerId) throws Exception {
        byte[] bytes = node.cafeSession(peerId);
        return bytes != null ? CafeSession.parseFrom(bytes) : null;
    }

    public CafeSessionList sessions() throws Exception {
        byte[] bytes = node.cafeSessions();
        return CafeSessionList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    public CafeSession refreshSession(String peerId) throws Exception {
        byte[] bytes = node.refreshCafeSession(peerId);
        return bytes != null ? CafeSession.parseFrom(bytes) : null;
    }

    public void deregister(String peerId) throws Exception {
        node.deregisterCafe(peerId);
    }

    public void checkMessages() throws Exception {
        node.checkCafeMessages();
    }
}
