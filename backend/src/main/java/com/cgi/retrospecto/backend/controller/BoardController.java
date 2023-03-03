package com.cgi.retrospecto.backend.controller;

import com.cgi.retrospecto.backend.domain.RetroBoard;
import com.cgi.retrospecto.backend.domain.RetroBoardMessage;
import com.cgi.retrospecto.backend.service.RetroBoardHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class BoardController {

    @Autowired
    private RetroBoardHandler retroBoardHandler;

    @MessageMapping("/board/{boardId}/card.add")
    @SendTo("/topic/board/{boardId}")
    public RetroBoardMessage addRetroBoardCard(@DestinationVariable int boardId, @Payload RetroBoardMessage retroBoardMessage) {
        return retroBoardHandler.addRetroBoardCard(boardId, retroBoardMessage);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/{id}", method = RequestMethod.GET)
    public RetroBoard getRetroBoard(@PathVariable int id) {
        return retroBoardHandler.getRetroBoard(id);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/number", method = RequestMethod.GET)
    public int getNumberOfActiveRetroBoards() {
        return retroBoardHandler.getNumberOfActiveRetroBoards();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/board/create/{author}", method = RequestMethod.POST)
    public RetroBoard createBoard(@PathVariable String author) {
        return retroBoardHandler.createBoard(author);
    }
}
