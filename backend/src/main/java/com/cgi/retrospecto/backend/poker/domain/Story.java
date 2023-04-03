package com.cgi.retrospecto.backend.poker.domain;

import java.util.ArrayList;
import java.util.List;

public class Story {
    private Integer id;
    private String storyName;
    private List<VoteResult> voteResults = new ArrayList<>();
    private boolean disabled;

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

    public String getStoryName() {
        return storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    public List<VoteResult> getVoteResults() {
        return voteResults;
    }

    public void setVoteResults(List<VoteResult> voteResults) {
        this.voteResults = voteResults;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    public String toString() {
        return "Story{" +
                "id=" + id +
                ", story='" + storyName + '\'' +
                ", voteResults=" + voteResults +
                ", disabled=" + disabled +
                '}';
    }
}
