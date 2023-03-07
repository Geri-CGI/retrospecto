package com.cgi.retrospecto.backend.domain;

public class RetroBoardMessage {
    private String username;

    private String cardMessage;

    private ColumnType columnType;

    private int index;

    private int likes = 0;

    private int dislikes = 0;

    public RetroBoardMessage(String username, String cardMessage) {
        this.username = username;
        this.cardMessage = cardMessage;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumType(ColumnType columType) {
        this.columnType = columType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCardMessage() {
        return cardMessage;
    }

    public void setCardMessage(String cardMessage) {
        this.cardMessage = cardMessage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLikes() {
        return likes;
    }

    public void increaseLikes() {
        this.likes += 1;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void increaseDislikes() {
        this.dislikes += 1;
    }

    public enum ColumnType {
        EXPECT,
        WELL,
        NOT_WELL,
        TRY
    }
}
