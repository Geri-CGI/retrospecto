package com.cgi.retrospecto.backend.poker.controller.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomDTO {
    private int id;
    private String author;
    private List<StoryDTO> stories;
    private Integer selectedStoryId;
    private Set<String> users = new HashSet<>();

    public RoomDTO() {
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

    public List<StoryDTO> getStories() {
        return stories;
    }

    public void setStories(List<StoryDTO> stories) {
        this.stories = stories;
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
}
