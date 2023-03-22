package com.cgi.retrospecto.backend.poker.controller.dto;

public class VoteDTO {
    private String username;
    private int value;

    public VoteDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
