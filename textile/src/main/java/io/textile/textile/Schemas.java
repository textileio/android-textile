package io.textile.textile;

import io.textile.pb.Model.Node;
import io.textile.pb.Model.FileIndex;
import mobile.Mobile_;

public class Schemas extends NodeDependent {

    Schemas(Mobile_ node) {
        super(node);
    }

    public FileIndex add(Node schemaNode) throws Exception {
        byte[] bytes = node.addSchema(schemaNode.toByteArray());
        return FileIndex.parseFrom(bytes);
    }
}
