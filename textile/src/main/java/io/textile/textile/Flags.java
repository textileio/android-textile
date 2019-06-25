package io.textile.textile;

import mobile.Mobile_;

/**
 * Provides access to Textile flags related APIs
 */
public class Flags extends NodeDependent {

    Flags(final Mobile_ node) {
        super(node);
    }

    /**
     * Flag any data by block
     * @param blockId The id of the Block to flag
     * @return The hash of the new flag block
     * @throws Exception The exception that occurred
     */
    public String add(final String blockId) throws Exception {
        return node.addFlag(blockId);
    }
}
