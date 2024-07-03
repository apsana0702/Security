package com.bourntec.security.UserProfile;

public class UserProfile {
	private String username;

    // Constructors
    public UserProfile() {
    }

    public UserProfile(String username) {
        this.username = username;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
