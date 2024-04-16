package vttp.server.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.server.Model.Notification;

@Repository
public class NotificationRepository {

    @Autowired
    JdbcTemplate template;

    public static final String SQL_ADD_NEW_NOTIFICATIONS = """
            insert into notifications(notificationId, username, text, timestamp) values (?,?,?,?)
            """;

    public static final String SQL_GET_NUMBER_UNREAD_NOTIFS = """
            SELECT COUNT(*) AS unread_notification_count
            FROM notifications
            WHERE username = ? AND notificationRead = false;
                        """;

    public long addNewNotification(Notification notification) {
        System.out.println("format of timestamp" + notification.getTimeStamp().getTime());
        long count = template.update(SQL_ADD_NEW_NOTIFICATIONS,
                notification.getId(),
                notification.getUsername(),
                notification.getText(),
                notification.getTimeStamp().getTime());

        return count;
    }

    public SqlRowSet getNumberOfUnreadNotifications(String username) {
        SqlRowSet rs = template.queryForRowSet(SQL_GET_NUMBER_UNREAD_NOTIFS, username);
        return rs;
    }

    public static final String SQL_GET_NOTIFICATIONS = """
            SELECT * FROM notifications
            WHERE username = ? AND notificationRead = false
            ORDER BY timestamp DESC
            """;

    public SqlRowSet getNotifications(String username) {
        SqlRowSet rowset = template.queryForRowSet(SQL_GET_NOTIFICATIONS, username);
        System.out.println(rowset);
        return rowset;
    }

    public static final String SQL_READ_NOTIFICATION = """
            update notifications
            set notificationRead = true
            where notificationId = ?
            """;

    public long readNotification(String notifId) {
        System.out.println("read notif");
        long count = template.update(SQL_READ_NOTIFICATION, notifId);
        return count;
    }


    public static final String SQL_GET_SINGLE_NOTIFICATION="""
        SELECT * from notifications
        WHERE notificationId = ?
        """;

    public SqlRowSet getSingleNotification(String notifId){
        SqlRowSet rowSet = template.queryForRowSet(SQL_GET_SINGLE_NOTIFICATION, notifId);
        return rowSet;
    }

}
