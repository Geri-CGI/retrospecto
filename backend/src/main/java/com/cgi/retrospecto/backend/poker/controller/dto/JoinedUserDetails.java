package com.cgi.retrospecto.backend.poker.controller.dto;

public class JoinedUserDetails {

    private String username;

    public JoinedUserDetails(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
