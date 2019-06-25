package io.textile.textile;

import io.textile.pb.View.TextList;
import mobile.Mobile_;

/**
 * Provides access to Textile messages related APIs
 */
public class Messages extends NodeDependent {

    Messages(final Mobile_ node) {
        super(node);
    }

    /**
     * Add a text message to a thread
     * @param threadId The id of the thread to add the message to
     * @param body The body of the message
     * @return The hash of the newly created message block
     * @throws Exception The exception that occurred
     */
    public String add(final String threadId, final String body) throws Exception {
        return node.addMessage(threadId, body);
    }

    /**
     * List messages for a thread
     * @param offset The offset to query from
     * @param limit The max number of messages to return
     * @param threadId The id of the thread to query
     * @return An object containing a list of messages
     * @throws Exception The exception that occurred
     */
    public TextList list(final String offset, long limit, final String threadId) throws Exception {
        final byte[] bytes = node.messages(offset, limit, threadId);
        return TextList.parseFrom(bytes != null ? bytes : new byte[0]);
    }
}
