package com.cgi.retrospecto.backend.board.dto;

public class RetroBoardInput {
    private int id;
    private String author;
    private String firstColumnName;
    private String secondColumnName;
    private String thirdColumnName;
    private String fourthColumnName;

    public RetroBoardInput(int id, String author, String firstColumnName, String secondColumnName, String thirdColumnName, String fourthColumnName) {
        this.id = id;
        this.author = author;
        this.firstColumnName = firstColumnName;
        this.secondColumnName = secondColumnName;
        this.thirdColumnName = thirdColumnName;
        this.fourthColumnName = fourthColumnName;
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

    public String getFirstColumnName() {
        return firstColumnName;
    }

    public void setFirstColumnName(String firstColumnName) {
        this.firstColumnName = firstColumnName;
    }

    public String getSecondColumnName() {
        return secondColumnName;
    }

    public void setSecondColumnName(String secondColumnName) {
        this.secondColumnName = secondColumnName;
    }

    public String getThirdColumnName() {
        return thirdColumnName;
    }

    public void setThirdColumnName(String thirdColumnName) {
        this.thirdColumnName = thirdColumnName;
    }

    public String getFourthColumnName() {
        return fourthColumnName;
    }

    public void setFourthColumnName(String fourthColumnName) {
        this.fourthColumnName = fourthColumnName;
    }
}
