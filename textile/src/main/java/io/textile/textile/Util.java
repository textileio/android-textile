package io.textile.textile;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;

import java.util.Date;

import io.textile.pb.View.Comment;
import io.textile.pb.View.FeedItem;
import io.textile.pb.View.Files;
import io.textile.pb.View.Ignore;
import io.textile.pb.View.Join;
import io.textile.pb.View.Leave;
import io.textile.pb.View.Like;
import io.textile.pb.View.Text;

public class Util {

    public static Date timestampToDate(Timestamp timestamp) {
        double milliseconds = timestamp.getSeconds() * 1e3 + timestamp.getNanos() / 1e6;
        return new Date((long)milliseconds);
    }

    static FeedItemData feedItemData(FeedItem feedItem) throws Exception {
        FeedItemData feedItemData;
        String typeUrl = feedItem.getPayload().getTypeUrl();
        ByteString bytes = feedItem.getPayload().getValue();
        if (typeUrl.equals("/Text")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.TEXT;
            feedItemData.text = Text.parseFrom(bytes);
        } else if (typeUrl.equals("/Comment")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.COMMENT;
            feedItemData.comment = Comment.parseFrom(bytes);
        } else if (typeUrl.equals("/Like")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.LIKE;
            feedItemData.like = Like.parseFrom(bytes);
        } else if (typeUrl.equals("/Files")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.FILES;
            feedItemData.files = Files.parseFrom(bytes);
        } else if (typeUrl.equals("/Ignore")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.IGNORE;
            feedItemData.ignore = Ignore.parseFrom(bytes);
        } else if (typeUrl.equals("/Join")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.JOIN;
            feedItemData.join = Join.parseFrom(bytes);
        } else if (typeUrl.equals("/Leave")) {
            feedItemData = new FeedItemData();
            feedItemData.block = feedItem.getBlock();
            feedItemData.type = FeedItemType.LEAVE;
            feedItemData.leave = Leave.parseFrom(bytes);
        } else {
            throw new Exception("Unknown feed item typeUrl: " + typeUrl);
        }
        return feedItemData;
    }
}
