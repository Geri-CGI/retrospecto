package com.cgi.retrospecto.backend.board.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RetroBoardMessage {
    private static final AtomicInteger idGenerator = new AtomicInteger(1000);

    private String username;

    private String cardMessage;

    private ColumnType columnType;

    private int index;

    private int likes = 0;

    private int dislikes = 0;
    private int uniqueId;

    private Map<String, String> likeLog = new HashMap<>();

    public RetroBoardMessage(String username, String cardMessage) {
        this.username = username;
        this.cardMessage = cardMessage;
        this.uniqueId = idGenerator.getAndIncrement();
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

    public void increaseLikes(String username) {
        this.likes += 1;
        getLikeLog().put(username, "LIKE");
    }

    public void decreaseReaction(String username) {
        switch (getLikeLog().get(username)) {
            case "LIKE" -> decreaseLikes(username);
            case "DISLIKE" -> decreaseDislikes(username);
        }
    }

    public void decreaseLikes(String username) {
        this.likes -= 1;
        getLikeLog().remove(username);
    }

    public int getDislikes() {
        return dislikes;
    }

    public void increaseDislikes(String username) {
        this.dislikes += 1;
        getLikeLog().put(username, "DISLIKE");
    }

    public void decreaseDislikes(String username) {
        this.dislikes -= 1;
        getLikeLog().remove(username);
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public enum ColumnType {
        EXPECT,
        WELL,
        NOT_WELL,
        TRY
    }

    public Map<String, String> getLikeLog() {
        return likeLog;
    }
}
