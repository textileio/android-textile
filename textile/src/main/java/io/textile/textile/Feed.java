package io.textile.textile;

import java.util.ArrayList;

import io.textile.pb.View.FeedItem;
import io.textile.pb.View.FeedRequest;
import io.textile.pb.View.FeedItemList;
import mobile.Mobile_;

/**
 * Provides access to Textile feed related APIs
 */
public class Feed extends NodeDependent {

    Feed(final Mobile_ node) {
        super(node);
    }

    /**
     * List thread items using a Feed view of the data, useful for timeline based views of thread data
     * @param request An object that configures the request for feed data
     * @return A list of feed items
     * @throws Exception The exception that occurred
     */
    public ArrayList<FeedItemData> list(final FeedRequest request) throws Exception {
        final byte[] bytes = node.feed(request.toByteArray());
        final FeedItemList list = FeedItemList.parseFrom(bytes != null ? bytes : new byte[0]);
        final ArrayList<FeedItemData> results = new ArrayList<>();
        for (final FeedItem feedItem : list.getItemsList()) {
            results.add(Util.feedItemData(feedItem));
        }
        return results;
    }
}
