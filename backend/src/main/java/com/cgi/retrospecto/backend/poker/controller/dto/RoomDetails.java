package com.cgi.retrospecto.backend.poker.controller.dto;

import com.cgi.retrospecto.backend.poker.domain.Story;
import com.cgi.retrospecto.backend.poker.domain.User;

import java.util.ArrayList;
import java.util.List;

public class RoomDetails {
    private int id;
    private User author;
    private List<Story> stories;
    private Integer selectedStoryId;
    private List<UserDetails> users = new ArrayList<>();

    public RoomDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
    public Integer getSelectedStoryId() {
        return selectedStoryId;
    }

    public void setSelectedStoryId(Integer selectedStoryId) {
        this.selectedStoryId = selectedStoryId;
    }

    public List<UserDetails> getUsers() {
        return users;
    }

    public void setUsers(List<UserDetails> users) {
        this.users = users;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
