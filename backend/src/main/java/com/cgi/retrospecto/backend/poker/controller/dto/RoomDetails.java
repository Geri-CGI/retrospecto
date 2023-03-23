package com.cgi.retrospecto.backend.poker.controller.dto;

import com.cgi.retrospecto.backend.poker.domain.Story;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomDetails {
    private int id;
    private String author;
    private List<Story> stories;
    private Integer selectedStoryId;
    private Set<String> users = new HashSet<>();

    public RoomDetails() {
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
    public Integer getSelectedStoryId() {
        return selectedStoryId;
    }

    public void setSelectedStoryId(Integer selectedStoryId) {
        this.selectedStoryId = selectedStoryId;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
