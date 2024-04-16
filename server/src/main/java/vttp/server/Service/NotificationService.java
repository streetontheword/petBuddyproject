package vttp.server.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp.server.Configuration.ThreadReplyWebSocketHandler;
import vttp.server.Model.Notification;
import vttp.server.Repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private ThreadReplyWebSocketHandler webSocketHandler;

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    NotificationRepository notifRepo;

    public void sendThreadReplyNotification(String userId, String threadId) {
        messagingTemplate.convertAndSendToUser(userId, "/thread-reply", "new reply on thread" + threadId);
    }

    public void addNewNotifications(String requestBody) {
        String id = UUID.randomUUID().toString().substring(0, 8);

        JsonReader jsonReader = Json.createReader(new StringReader(requestBody));
        JsonObject jsonObject = jsonReader.readObject();

        // recipient username
        String username = jsonObject.getString("username");
        String text = jsonObject.getString("text");
        boolean read = false;

        Date timestamp = new Date();
        Notification notification = new Notification(id, username, text, read, timestamp);
        // System.out.println(notification);
        notifRepo.addNewNotification(notification);

        Integer count = getCountOfUnreadMessages(username);
        try {
            webSocketHandler.updateUnreadNotifsCount(username, count);
        } catch (Exception ex) {
            System.out.println("User not connected");
        }
    }

    public Integer getCountOfUnreadMessages(String username) {
        SqlRowSet rowSet = notifRepo.getNumberOfUnreadNotifications(username);
        int count = 0;
        if (rowSet.next()) {
            count = rowSet.getInt("unread_notification_count");
            System.out.println("count from sql "+count);
        }
        return count;
    }

    public String getNotifications(String username) {
        SqlRowSet rowset = notifRepo.getNotifications(username);
        String body = processGetNotificationsRequest(rowset);

        return body;
    }

    public String getNumbeOfUnreadNotifications(String username) {
        Integer count = getCountOfUnreadMessages(username);
        JsonObjectBuilder JOB = Json.createObjectBuilder();
        JOB.add("count", count);
        return JOB.build().toString();
    }

    public String processGetNotificationsRequest(SqlRowSet rowSet) {
        JsonArrayBuilder JAB = Json.createArrayBuilder();
        while (rowSet.next()) {
            String id = rowSet.getString("notificationId");
            String username = rowSet.getString("username");
            String text = rowSet.getString("text");
            boolean notification_read = rowSet.getBoolean("notificationRead");
            Date timestamp = new Date(rowSet.getLong("timestamp"));

            Notification notification = new Notification(id, username, text, notification_read, timestamp);
            JsonObjectBuilder JOB = Json.createObjectBuilder();
            JsonObject jsonobject = JOB.add("id", notification.getId())
                    .add("username", notification.getUsername())
                    .add("text", notification.getText())
                    .add("notificationRead", notification.isNotificationRead())
                    .add("timestamp", notification.getTimeStamp().getTime()).build();

            JAB.add(jsonobject);
        }

        return JAB.build().toString();
    }

    public void readNotification(String notifId) throws IOException  {
        notifRepo.readNotification(notifId);
        String username = getSingleNotification(notifId);

        Integer count = getCountOfUnreadMessages(username);
    
        webSocketHandler.updateUnreadNotifsCount(username, count);
    }


    public String getSingleNotification(String notifId) {
        SqlRowSet rowSet = notifRepo.getSingleNotification(notifId);
        if(rowSet.next()){
            String username = rowSet.getString("username");
            return username;
        }
        return "error retrieving single notification in service"; 
    }
}
