package com.cgi.retrospecto.backend.poker.domain;

import java.util.ArrayList;
import java.util.List;

public class Story {
    private Integer id;
    private String story;
    private List<VoteResult> voteResults = new ArrayList<>();

    public Story() {
    }

    public int generateNextPokerResultId() {
        int maxId = voteResults.stream().mapToInt(VoteResult::getId).max().orElse(0);
        return ++maxId;
    }

    public VoteResult getOpenPokerVoteResult() {
        return voteResults.stream().filter(vr -> !vr.isLocked()).findFirst().orElse(null);
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

    public List<VoteResult> getVoteResults() {
        return voteResults;
    }

    public void setVoteResults(List<VoteResult> voteResults) {
        this.voteResults = voteResults;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", story='" + story + '\'' +
                ", voteResults=" + voteResults +
                '}';
    }
}
