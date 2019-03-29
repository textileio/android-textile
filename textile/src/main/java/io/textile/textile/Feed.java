package io.textile.textile;

import io.textile.pb.View.FeedRequest;
import io.textile.pb.View.FeedItemList;
import mobile.Mobile_;

public class Feed extends NodeDependent {

    Feed(Mobile_ node) {
        super(node);
    }

    public FeedItemList list(FeedRequest request) throws Exception {
        byte[] bytes = this.node.feed(request.toByteArray());
        return FeedItemList.parseFrom(bytes);
    }
}
