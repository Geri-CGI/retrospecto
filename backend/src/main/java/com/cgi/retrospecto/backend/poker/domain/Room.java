package com.cgi.retrospecto.backend.poker.domain;

import com.cgi.retrospecto.backend.poker.domain.comparator.UserComparatorByName;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Room {
    private int id;
    private User author;
    private List<Story> stories = new ArrayList<>();
    private LocalDateTime lastActionSubmittedTime;
    private Set<User> users = new TreeSet<>(new UserComparatorByName());
    private Story selectedStory;

    public Room() {
    }

    public void addUser(User author) {
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
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

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
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
