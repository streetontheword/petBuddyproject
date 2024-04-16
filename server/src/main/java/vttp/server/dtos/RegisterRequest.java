package vttp.server.dtos;

import java.util.UUID;

public class RegisterRequest {
    private String userId; 
    private String email;
    private String firstName; 
    private String lastName; 
    private String userName; 
    private String password;
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public RegisterRequest(String userId, String email, String firstName, String lastName, String userName,
            String password) {
        this.userId = UUID.randomUUID().toString().substring(0,8);
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
    }
    public RegisterRequest() {
    }

    
}
