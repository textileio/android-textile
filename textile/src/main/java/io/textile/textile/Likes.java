package io.textile.textile;

import mobile.Mobile_;

/**
 * Provides access to Textile likes related APIs
 */
public class Likes extends NodeDependent {

    Likes(final Mobile_ node) {
        super(node);
    }

    /**
     * Add a like for a specific block
     * @param blockId The id of the block you want to add a like to
     * @return The hash of the newly created like block
     * @throws Exception The exception that occurred
     */
    public String add(final String blockId) throws Exception {
        return node.addLike(blockId);
    }
}
