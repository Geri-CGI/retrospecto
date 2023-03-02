package com.cgi.retrospecto.backend.service;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class RetroBoardKeeper {
    private static final HashMap<Integer, RetroBoard> retroBoardsMap = new HashMap<>();

    public RetroBoardKeeper() {
    }

    public void addBoard(RetroBoard retroBoard) {
        retroBoardsMap.put(retroBoard.getId(), retroBoard);
    }

    public RetroBoard getRetroBoard(int id) {
        return retroBoardsMap.get(id);
    }
}
