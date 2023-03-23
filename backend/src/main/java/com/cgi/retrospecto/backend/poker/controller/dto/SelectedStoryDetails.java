package com.cgi.retrospecto.backend.poker.controller.dto;

public class SelectedStoryDetails {

    private int roomId;

    private int storyId;

    public SelectedStoryDetails() {
    }

    public SelectedStoryDetails(int roomId, int storyId) {
        this.roomId = roomId;
        this.storyId = storyId;
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
}
