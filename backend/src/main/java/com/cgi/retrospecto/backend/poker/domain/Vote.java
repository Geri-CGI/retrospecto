package com.cgi.retrospecto.backend.poker.domain;

public class Vote {
    private User user;
    private int value;

    public Vote() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                "user='" + user + '\'' +
                ", value=" + value +
                '}';
    }
}
