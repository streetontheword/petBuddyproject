package vttp.server.Model;

import java.util.List;

public class Threads {
     private String id;
    private String username;
    private String title;
    private String text;
    private String timestamp;
    private String userurl; 
    private List<Comment> comments;

    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;

    }
   
   
    public String getUserurl() {
        return userurl;
    }
    public void setUserurl(String userurl) {
        this.userurl = userurl;
    }
    
    public Threads() {
    }
    public Threads(String id, String username, String title, String text, String timestamp, String userurl,
            List<Comment> comments) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.text = text;
        this.timestamp = timestamp;
        this.userurl = userurl;
        this.comments = comments;
    }

    
    @Override
    public String toString() {
        return "Threads [id=" + id + ", username=" + username + ", title=" + title + ", text=" + text + ", timestamp="
                + timestamp + ", userurl=" + userurl + ", comments=" + comments + "]";
    }

    

  
    
    


    
}
