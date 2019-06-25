package io.textile.textile;

import io.textile.pb.Model.Node;
import io.textile.pb.Model.FileIndex;
import mobile.Mobile_;

/**
 * Provides access to Textile schemas related APIs
 */
public class Schemas extends NodeDependent {

    Schemas(final Mobile_ node) {
        super(node);
    }

    /**
     * Add a new schema
     * @param schemaNode The node that describes the new schema to add
     * @return The FileIndex representing the added schema
     * @throws Exception The exception that occurred
     */
    public FileIndex add(final Node schemaNode) throws Exception {
        final byte[] bytes = node.addSchema(schemaNode.toByteArray());
        return FileIndex.parseFrom(bytes);
    }
}
