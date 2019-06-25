package io.textile.textile;

import io.textile.pb.Model.NotificationList;
import mobile.Mobile_;

/**
 * Provides access to Textile notifications related APIs
 */
public class Notifications extends NodeDependent {

    Notifications(final Mobile_ node) {
        super(node);
    }

    /**
     * List notifications
     * @param offset The offset to query from
     * @param limit The max number of notifications to return
     * @return An object containing a list of notifications
     * @throws Exception The exception that occurred
     */
    public NotificationList list(final String offset, final long limit) throws Exception {
        final byte[] bytes = node.notifications(offset, limit);
        return NotificationList.parseFrom(bytes != null ? bytes : new byte[0]);
    }

    /**
     * @return The number of unread notifications
     */
    public long countUnread() {
        return node.countUnreadNotifications();
    }

    /**
     * Mark a notification as read
     * @param notificationId The id of the notification to mark as read
     * @throws Exception The exception that occurred
     */
    public void read(final String notificationId) throws Exception {
        node.readNotification(notificationId);
    }

    /**
     * Mark all notifications as read
     * @throws Exception The exception that occurred
     */
    public void readAll() throws Exception {
        node.readAllNotifications();
    }

    /**
     * Accept an invite via an invite notification
     * @param notificationId The id of the invite notification
     * @return The hash of the newly created thread join block
     * @throws Exception The exception that occurred
     */
    public String acceptInvite(final String notificationId) throws Exception {
        return node.acceptInviteViaNotification(notificationId);
    }

    /**
     * Ignore an invite via an invite notification
     * @param notificationId The id of the invite notification
     * @throws Exception The exception that occurred
     */
    public void ignoreInvite(final String notificationId) throws Exception {
        node.ignoreInviteViaNotification(notificationId);
    }
}
