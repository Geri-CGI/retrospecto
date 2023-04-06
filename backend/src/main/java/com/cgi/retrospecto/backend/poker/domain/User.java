package com.cgi.retrospecto.backend.poker.domain;

import java.util.Objects;

public class User {
    private String username;
    private Integer sessionId;

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(sessionId, user.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, sessionId);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", sessionId=" + sessionId +
                '}';
    }
}
