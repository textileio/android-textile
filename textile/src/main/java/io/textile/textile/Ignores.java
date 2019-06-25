package io.textile.textile;

import mobile.Mobile_;

/**
 * Provides access to Textile ignores related APIs
 */
public class Ignores extends NodeDependent {

    Ignores(final Mobile_ node) {
        super(node);
    }

    /**
     * Ignore data by block
     * @param blockId The id of the block you want to ignore
     * @return The hash of the new ignore block
     * @throws Exception The exception that occurred
     */
    public String add(final String blockId) throws Exception {
        return node.addIgnore(blockId);
    }
}
