package vttp.server.dtos;

import vttp.server.enums.UserRole;

public class AuthenticationResponse {
    private String jwt; 
    private String name; 
    private UserRole role; 
    private String userID;
    
    public String getJwt() {
        return jwt;
    }
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }
    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public AuthenticationResponse() {
    }
    public AuthenticationResponse(String jwt, String name, UserRole role, String userID) {
        this.jwt = jwt;
        this.name = name;
        this.role = role;
        this.userID = userID;
    }
    @Override
    public String toString() {
        return "AuthenticationResponse [jwt=" + jwt + ", name=" + name + ", role=" + role + ", userID=" + userID + "]";
    } 

    
    
}
