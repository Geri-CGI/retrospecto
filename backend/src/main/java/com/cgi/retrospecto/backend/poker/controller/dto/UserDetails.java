package com.cgi.retrospecto.backend.poker.controller.dto;

public class UserDetails {
    private String username;

    public UserDetails(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
