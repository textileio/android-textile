package io.textile.textile;

import io.textile.pb.View.Comment;
import io.textile.pb.View.Files;
import io.textile.pb.View.Ignore;
import io.textile.pb.View.Join;
import io.textile.pb.View.Leave;
import io.textile.pb.View.Like;
import io.textile.pb.View.Text;

public class FeedItemData {
    public FeedItemType type;
    public Text text;
    public Comment comment;
    public Like like;
    public Files files;
    public Ignore ignore;
    public Join join;
    public Leave leave;
}
