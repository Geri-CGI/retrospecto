package com.cgi.retrospecto.backend.service;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import com.cgi.retrospecto.backend.domain.RetroBoardMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

@Service
public class RetroBoardHandler {

    @Autowired
    private RetroBoardKeeper retroBoardKeeper;

    public RetroBoardMessage addRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().add(retroBoardMessage);
            case WELL -> retroBoard.getWentWellColumn().add(retroBoardMessage);
            case EXPECT -> retroBoard.getExpectColumn().add(retroBoardMessage);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().add(retroBoardMessage);
        }
        return retroBoardMessage;
    }

    public RetroBoard getRetroBoard(@PathVariable int id) {
        return retroBoardKeeper.getRetroBoard(id);
    }

    public RetroBoard createBoard(@PathVariable String author) {
        int id = new Random().nextInt(900000) + 100000;
        RetroBoard newRetroBoard = new RetroBoard(id, author, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        retroBoardKeeper.addBoard(newRetroBoard);
        return newRetroBoard;
    }

    public int getNumberOfActiveRetroBoards() {
        return retroBoardKeeper.getNumberOfActiveRetroBoards();
    }

    public RetroBoardMessage deleteRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().remove(index);
            case WELL -> retroBoard.getWentWellColumn().remove(index);
            case EXPECT -> retroBoard.getExpectColumn().remove(index);
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().remove(index);
        }
        return retroBoardMessage;
    }

    public RetroBoardMessage editRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case WELL -> retroBoard.getWentWellColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case EXPECT -> retroBoard.getExpectColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
            case NOT_WELL ->
                    retroBoard.getDidNotGoWellColumn().get(index).setCardMessage(retroBoardMessage.getCardMessage());
        }
        return retroBoardMessage;
    }

    public RetroBoardMessage likeRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).increaseLikes();
            case WELL -> retroBoard.getWentWellColumn().get(index).increaseLikes();
            case EXPECT -> retroBoard.getExpectColumn().get(index).increaseLikes();
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().get(index).increaseLikes();
        }
        return retroBoardMessage;
    }

    public RetroBoardMessage dislikeRetroBoardCard(int boardId, RetroBoardMessage retroBoardMessage) {
        final RetroBoard retroBoard = retroBoardKeeper.getRetroBoard(boardId);
        int index = retroBoardMessage.getIndex();
        retroBoard.setLastActionSubmittedTime(LocalDateTime.now());
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoard.getWantToTryColumn().get(index).increaseDislikes();
            case WELL -> retroBoard.getWentWellColumn().get(index).increaseDislikes();
            case EXPECT -> retroBoard.getExpectColumn().get(index).increaseDislikes();
            case NOT_WELL -> retroBoard.getDidNotGoWellColumn().get(index).increaseDislikes();
        }
        return retroBoardMessage;
    }
}
