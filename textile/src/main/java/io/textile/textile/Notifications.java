package io.textile.textile;

import io.textile.pb.Model.NotificationList;
import mobile.Mobile_;

public class Notifications extends NodeDependent {

    Notifications(Mobile_ node) {
        super(node);
    }

    public NotificationList list(String offset, long limit) throws Exception {
        byte[] bytes = node.notifications(offset, limit);
        return NotificationList.parseFrom(bytes);
    }

    public long countUnread() {
        return node.countUnreadNotifications();
    }

    public void read(String notificationId) throws Exception {
        node.readNotification(notificationId);
    }

    public void readAll() throws Exception {
        node.readAllNotifications();
    }

    public String acceptInvite(String id) throws Exception {
        return node.acceptInviteViaNotification(id);
    }

    public void ignoreInvite(String id) throws Exception {
        node.ignoreInviteViaNotification(id);
    }
}
