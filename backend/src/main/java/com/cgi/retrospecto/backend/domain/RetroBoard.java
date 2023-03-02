package com.cgi.retrospecto.backend.domain;

import java.util.List;

public class RetroBoard {
    private int id;
    private String author;

    private List<RetroBoardMessage> expectColumn;

    private List<RetroBoardMessage> wentWellColumn;

    private List<RetroBoardMessage> didNotGoWellColumn;

    private List<RetroBoardMessage> wantToTryColumn;

    public RetroBoard(int id, String author, List<RetroBoardMessage> expectColumn, List<RetroBoardMessage> wentWellColumn, List<RetroBoardMessage> didNotGoWellColumn, List<RetroBoardMessage> wantToTryColumn) {
        this.id = id;
        this.author = author;
        this.expectColumn = expectColumn;
        this.wentWellColumn = wentWellColumn;
        this.didNotGoWellColumn = didNotGoWellColumn;
        this.wantToTryColumn = wantToTryColumn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<RetroBoardMessage> getExpectColumn() {
        return expectColumn;
    }

    public void setExpectColumn(List<RetroBoardMessage> expectColumn) {
        this.expectColumn = expectColumn;
    }

    public List<RetroBoardMessage> getWentWellColumn() {
        return wentWellColumn;
    }

    public void setWentWellColumn(List<RetroBoardMessage> wentWellColumn) {
        this.wentWellColumn = wentWellColumn;
    }

    public List<RetroBoardMessage> getDidNotGoWellColumn() {
        return didNotGoWellColumn;
    }

    public void setDidNotGoWellColumn(List<RetroBoardMessage> didNotGoWellColumn) {
        this.didNotGoWellColumn = didNotGoWellColumn;
    }

    public List<RetroBoardMessage> getWantToTryColumn() {
        return wantToTryColumn;
    }

    public void setWantToTryColumn(List<RetroBoardMessage> wantToTryColumn) {
        this.wantToTryColumn = wantToTryColumn;
    }
}
