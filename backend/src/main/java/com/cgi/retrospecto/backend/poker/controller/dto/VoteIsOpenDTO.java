package com.cgi.retrospecto.backend.poker.controller.dto;

public class VoteIsOpenDTO {
    private boolean open;

    public VoteIsOpenDTO() {
    }

    public VoteIsOpenDTO(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
