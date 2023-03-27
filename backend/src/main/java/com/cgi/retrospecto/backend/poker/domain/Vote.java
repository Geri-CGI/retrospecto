package com.cgi.retrospecto.backend.poker.domain;

public class Vote {
    private String username;
    private int value;

    public Vote() {
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

    @Override
    public String toString() {
        return "PokerVote{" +
                "username='" + username + '\'' +
                ", value=" + value +
                '}';
    }
}
