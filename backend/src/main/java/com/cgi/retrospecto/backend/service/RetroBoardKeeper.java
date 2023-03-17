package com.cgi.retrospecto.backend.service;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
@EnableScheduling
public class RetroBoardKeeper {
    private static final HashMap<Integer, RetroBoard> retroBoardsMap = new HashMap<>();

    public RetroBoardKeeper() {
    }

    public void addBoard(RetroBoard retroBoard) {
        retroBoardsMap.put(retroBoard.getId(), retroBoard);
    }

    public RetroBoard getRetroBoard(int id) {
        if (retroBoardsMap.containsKey(id)) {
            return retroBoardsMap.get(id);
        }
        return null;
    }

    public int getNumberOfActiveRetroBoards() {
        return retroBoardsMap.size();
    }

    @Scheduled(fixedRate = 3600000)
    private void purgeInactiveRetroBoards() {
        for (Map.Entry<Integer, RetroBoard> entry : retroBoardsMap.entrySet()) {
            LocalDateTime fromDate = entry.getValue().getLastActionSubmittedTime();
            LocalDateTime toDate = LocalDateTime.now();
            if (ChronoUnit.HOURS.between(fromDate, toDate) > 1) {
                retroBoardsMap.remove(entry.getKey());
            }
        }
    }
}
