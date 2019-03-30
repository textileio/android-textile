package io.textile.textile;

import mobile.Mobile_;

public class Ignores extends NodeDependent {

    Ignores(Mobile_ node) {
        super(node);
    }

    public String add(String blockId) throws Exception {
        return node.addIgnore(blockId);
    }
}
