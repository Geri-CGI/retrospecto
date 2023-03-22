package com.cgi.retrospecto.backend.poker.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class VoteResultDTO {
    private int id;
    private List<VoteDTO> votes = new ArrayList<>();
    private boolean locked;

    public VoteResultDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VoteDTO> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteDTO> votes) {
        this.votes = votes;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
