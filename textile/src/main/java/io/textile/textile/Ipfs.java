package io.textile.textile;

import mobile.Mobile_;

public class Ipfs extends NodeDependent {

    Ipfs(Mobile_ node) {
        super(node);
    }

    public String peerId() throws Exception {
        return node.peerId();
    }

    public byte[] dataAtPath(String path) throws Exception {
        return node.dataAtPath(path);
    }
}
