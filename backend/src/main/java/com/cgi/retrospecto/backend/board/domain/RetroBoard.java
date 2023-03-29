package com.cgi.retrospecto.backend.board.domain;

import java.time.LocalDateTime;
import java.util.*;

public class RetroBoard {
    private int id;
    private String author;

    private List<RetroBoardMessage> expectColumn = new ArrayList<>();

    private List<RetroBoardMessage> wentWellColumn = new ArrayList<>();

    private List<RetroBoardMessage> didNotGoWellColumn = new ArrayList<>();

    private List<RetroBoardMessage> wantToTryColumn = new ArrayList<>();

    private LocalDateTime lastActionSubmittedTime;

    private Map<String, List<Integer>> likedRecords = new HashMap<>();

    private Set<String> users = new HashSet<>();

    private boolean isLocked = false;

    private int blurNumber = 5;

    private boolean blurActive;

    public RetroBoard(int id, String author) {
        this.id = id;
        this.author = author;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<RetroBoardMessage> getExpectColumn() {
        return expectColumn;
    }

    public void setExpectColumn(List<RetroBoardMessage> expectColumn) {
        this.expectColumn = expectColumn;
    }

    public List<RetroBoardMessage> getWentWellColumn() {
        return wentWellColumn;
    }

    public void setWentWellColumn(List<RetroBoardMessage> wentWellColumn) {
        this.wentWellColumn = wentWellColumn;
    }

    public List<RetroBoardMessage> getDidNotGoWellColumn() {
        return didNotGoWellColumn;
    }

    public void setDidNotGoWellColumn(List<RetroBoardMessage> didNotGoWellColumn) {
        this.didNotGoWellColumn = didNotGoWellColumn;
    }

    public List<RetroBoardMessage> getWantToTryColumn() {
        return wantToTryColumn;
    }

    public void setWantToTryColumn(List<RetroBoardMessage> wantToTryColumn) {
        this.wantToTryColumn = wantToTryColumn;
    }

    public LocalDateTime getLastActionSubmittedTime() {
        return lastActionSubmittedTime;
    }

    public void setLastActionSubmittedTime(LocalDateTime lastActionSubmittedTime) {
        this.lastActionSubmittedTime = lastActionSubmittedTime;
    }

    public Map<String, List<Integer>> getLikedRecords() {
        return likedRecords;
    }

    public Set<String> getUsers() {
        return users;
    }

    public int getBlurNumber() {
        return blurNumber;
    }

    public void setBlurNumber(int blurNumber) {
        this.blurNumber = blurNumber;
    }

    public boolean isBlurActive() {
        return blurActive;
    }

    public void setBlurActive(boolean blurActive) {
        this.blurActive = blurActive;
    }
}
