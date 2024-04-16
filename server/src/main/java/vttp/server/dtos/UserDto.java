package vttp.server.dtos;

import vttp.server.enums.UserRole;

public class UserDto {

    private String id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
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
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public UserDto() {
    }
    public UserDto(String id, String username, String email, String password, UserRole role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
    @Override
    public String toString() {
        return "UserDto [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
                + ", role=" + role + "]";
    }


    
}
