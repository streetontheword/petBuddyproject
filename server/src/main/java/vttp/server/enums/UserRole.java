package vttp.server.enums;

public enum UserRole {

    admin,
    user;

    public String getRoleAsString(UserRole role) {
        return role.name(); // This returns the name of the enum constant as a string
    }
}
