package com.cgi.retrospecto.backend.poker.controller.dto;

import com.cgi.retrospecto.backend.poker.domain.VoteResult;

public class OpenVote {
    private boolean open;
    private int storyId;
    private VoteResult voteResult;

    public OpenVote() {
    }

    public OpenVote(boolean open, int storyId, VoteResult voteResult) {
        this.open = open;
        this.storyId = storyId;
        this.voteResult = voteResult;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public VoteResult getVoteResult() {
        return voteResult;
    }

    public void setVoteResult(VoteResult voteResult) {
        this.voteResult = voteResult;
    }

    public int getStoryId() {
        return storyId;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }
}
