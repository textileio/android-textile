package io.textile.textile;

import io.textile.pb.View.TextList;
import mobile.Mobile_;

public class Messages extends NodeDependent {

    Messages(Mobile_ node) {
        super(node);
    }

    public String add(String threadId, String body) throws Exception {
        return node.addMessage(threadId, body);
    }

    public TextList list(String offset, long limit, String threadId) throws Exception {
        byte[] bytes = node.messages(offset, limit, threadId);
        return TextList.parseFrom(bytes != null ? bytes : new byte[0]);
    }
}
