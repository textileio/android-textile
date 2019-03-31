package io.textile.textile;

import mobile.Mobile_;

public class Likes extends NodeDependent {

    Like(Mobile_ node) {
        super(node);
    }

    public String add(String blockId) throws Exception {
        return node.addLike(blockId);
    }
}
