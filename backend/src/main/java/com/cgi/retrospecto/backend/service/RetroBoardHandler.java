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
}
