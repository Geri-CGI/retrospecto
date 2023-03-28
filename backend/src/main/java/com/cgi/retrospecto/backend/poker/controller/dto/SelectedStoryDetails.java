package com.cgi.retrospecto.backend.poker.controller.dto;

public class SelectedStoryDetails {

    private int roomId;

    private int storyId;

    private boolean disabled;

    public SelectedStoryDetails() {
    }

    public SelectedStoryDetails(int roomId, int storyId, boolean disabled) {
        this.roomId = roomId;
        this.storyId = storyId;
        this.disabled = disabled;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
