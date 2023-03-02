package com.cgi.retrospecto.backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class BoardController {

    @MessageMapping("/board/{boardId}/card.add")
    @SendTo("/topic/board/{boardId}")
    public String simple(@DestinationVariable String boardId, @Payload String cardMessage) {
        return "Bord id: " + boardId + ". Message is: " + cardMessage;
    }
}
