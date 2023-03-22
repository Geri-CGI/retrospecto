package com.cgi.retrospecto.backend.poker.domain;

import java.util.ArrayList;
import java.util.List;

public class VoteResult {
    private int id;
    private List<Vote> votes = new ArrayList<>();
    private boolean locked;

    public VoteResult() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "PokerVoteResult{" +
                "id=" + id +
                ", votes=" + votes +
                ", locked=" + locked +
                '}';
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }
}
