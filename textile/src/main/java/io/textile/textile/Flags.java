package io.textile.textile;

import mobile.Mobile_;

public class Flags extends NodeDependent {

    Flags(Mobile_ node) {
        super(node);
    }

    public String add(String blockId) throws Exception {
        return node.addFlag(blockId);
    }
}
