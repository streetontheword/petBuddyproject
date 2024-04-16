package vttp.server.Model;

import vttp.server.enums.UserRole;

public class User {
    
    private String userId; 
    private String email;
    private String firstName; 
    private String lastName; 
    private String userName; 
    private String password;
    private String role; 
    private String url; 
    

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
     public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    } 

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
  
    public User() {
    }

    
    public User(String userId, String email, String firstName, String lastName, String userName, String password,
            String role, String url) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.url = url;
    }
    @Override
    public String toString() {
        return "User [userId=" + userId + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName
                + ", userName=" + userName + ", password=" + password + ", role=" + role + ", url=" + url + "]";
    }

   

    
    
}

