package io.textile.textile;

import mobile.Mobile_;

public class Comments extends NodeDependent {

    Comments(Mobile_ node) {
        super(node);
    }

    public String add(String blockId, String body) throws Exception {
        return this.node.addComment(blockId, body);
    }
}
