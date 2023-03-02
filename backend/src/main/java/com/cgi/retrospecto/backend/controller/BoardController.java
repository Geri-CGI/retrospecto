package com.cgi.retrospecto.backend.controller;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import com.cgi.retrospecto.backend.domain.RetroBoardMessage;
import com.cgi.retrospecto.backend.service.RetroBoardKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class BoardController {

    @Autowired
    private RetroBoardKeeper retroBoardKeeper;

    @MessageMapping("/board/{boardId}/card.add")
    @SendTo("/topic/board/{boardId}")
    public RetroBoardMessage simple(@DestinationVariable int boardId, @Payload RetroBoardMessage retroBoardMessage) {
        switch (retroBoardMessage.getColumnType()) {
            case TRY -> retroBoardKeeper.getRetroBoard(boardId).getWantToTryColumn().add(retroBoardMessage);
            case WELL -> retroBoardKeeper.getRetroBoard(boardId).getWentWellColumn().add(retroBoardMessage);
            case EXPECT -> retroBoardKeeper.getRetroBoard(boardId).getExpectColumn().add(retroBoardMessage);
            case NOT_WELL -> retroBoardKeeper.getRetroBoard(boardId).getDidNotGoWellColumn().add(retroBoardMessage);
        }
        return retroBoardMessage;
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
    public RetroBoard getRetroBoard(@PathVariable int id) {
        return retroBoardKeeper.getRetroBoard(id);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/create/{id}/{author}", method = RequestMethod.POST)
    public RetroBoard createBoard(@PathVariable int id, @PathVariable String author) {
        RetroBoard newRetroBoard = new RetroBoard(id, author, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        retroBoardKeeper.addBoard(newRetroBoard);
        return newRetroBoard;
    }
}
