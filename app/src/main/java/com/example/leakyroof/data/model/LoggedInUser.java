package com.example.leakyroof.data.model;

import java.io.Serializable;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser implements Serializable {

    private String userId;
    private String displayName;
    private String password;

    public LoggedInUser(String userId, String displayName, String password) {
        this.userId = userId;
        this.displayName = displayName;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPassword() {
        return password;
    }
}
