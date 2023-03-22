package com.cgi.retrospecto.backend.poker.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Room {
    private int id;
    private String author;
    private List<Story> stories = new ArrayList<>();
    private LocalDateTime lastActionSubmittedTime;
    private Set<String> users = new HashSet<>();
    private Story selectedStory;

    public Room() {
    }

    public List<Integer> getStoriesIdsList() {
        return stories.stream().map(s -> s.getId()).collect(Collectors.toList());
    }

    public void addUser(String author) {
        users.add(author);
    }

    public void addStory(Story story) {
        stories.add(story);
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

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public LocalDateTime getLastActionSubmittedTime() {
        return lastActionSubmittedTime;
    }

    public void setLastActionSubmittedTime(LocalDateTime lastActionSubmittedTime) {
        this.lastActionSubmittedTime = lastActionSubmittedTime;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public Story getSelectedStory() {
        return selectedStory;
    }

    public void setSelectedStory(Story selectedStory) {
        this.selectedStory = selectedStory;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", stories=" + stories +
                ", lastActionSubmittedTime=" + lastActionSubmittedTime +
                ", users=" + users +
                ", selectedStory=" + selectedStory +
                '}';
    }
}
