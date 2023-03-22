package com.cgi.retrospecto.backend.poker.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class StoryDTO {
    private Integer id;
    private String story;
    private List<VoteResultDTO> voteResults = new ArrayList<>();

    public StoryDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public List<VoteResultDTO> getVoteResults() {
        return voteResults;
    }

    public void setVoteResults(List<VoteResultDTO> voteResults) {
        this.voteResults = voteResults;
    }
}
