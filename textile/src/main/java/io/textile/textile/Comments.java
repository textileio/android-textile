package io.textile.textile;

import mobile.Mobile_;

/**
 * Provides access to Textile comments related APIs
 */
public class Comments extends NodeDependent {

    Comments(final Mobile_ node) {
        super(node);
    }

    /**
     * Add a comment to any block by id
     * @param blockId The id of the block you want to add a comment to
     * @param body The text of the comment to add
     * @return The id of the new comment block
     * @throws Exception The exception that occurred
     */
    public String add(final String blockId, final String body) throws Exception {
        return node.addComment(blockId, body);
    }
}
