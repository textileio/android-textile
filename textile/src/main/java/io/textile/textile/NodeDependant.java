package io.textile.textile;

import mobile.Mobile_;

abstract class NodeDependant {

    Mobile_ node;

    public NodeDependant(Mobile_ node) {
        this.node = node;
    }
}
