package com.cgi.retrospecto.backend.poker.controller.dto;

public class IsVoteOpenStatus {
    private boolean open;

    public IsVoteOpenStatus() {
    }

    public IsVoteOpenStatus(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
