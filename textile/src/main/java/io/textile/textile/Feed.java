package io.textile.textile;

import io.textile.pb.View.FeedRequest;
import io.textile.pb.View.FeedItemList;
import mobile.Mobile_;

/**
 * Provides access to Textile feed related APIs
 */
public class Feed extends NodeDependent {

    Feed(Mobile_ node) {
        super(node);
    }

    /**
     * List thread items using a Feed view of the data, useful for timeline based views of thread data
     * @param request An object that configures the request for feed data
     * @return An object that contains a list of feed items
     * @throws Exception The exception that occurred
     */
    public FeedItemList list(FeedRequest request) throws Exception {
        byte[] bytes = node.feed(request.toByteArray());
        return FeedItemList.parseFrom(bytes != null ? bytes : new byte[0]);
    }
}
