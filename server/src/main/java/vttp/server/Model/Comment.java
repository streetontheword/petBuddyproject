package vttp.server.Model;

public class Comment {


    private String id;
    private String username;
    private String text;
    private String timestamp;
    
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
    public Comment(String id, String username, String text, String timestamp) {
        this.id = id;
        this.username = username;
        this.text = text;
        this.timestamp = timestamp;
    }
    public Comment() {
    }

    @Override
    public String toString() {
        return "Comment [id=" + id + ", username=" + username + ", text=" + text + ", timestamp=" + timestamp + "]";
    }
    
}
