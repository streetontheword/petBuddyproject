package vttp.server.Model;

import java.util.Date;

public class Notification {
    private String id;
    private String username; 
    private String text; 
    private boolean notificationRead; 
    private Date timeStamp;
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public boolean isNotificationRead() {
        return notificationRead;
    }
    public void setNotificationRead(boolean notificationRead) {
        this.notificationRead = notificationRead;
    }
    public Date getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Notification(String id, String username, String text, boolean notificationRead, Date timeStamp) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.notificationRead = notificationRead;
        this.timeStamp = timeStamp;
    }
    public Notification() {
    }
    @Override
    public String toString() {
        return "Notification [id=" + id + ", username=" + username + ", text=" + text  + ", timeStamp=" + timeStamp + "]";
    } 
   

    








}
