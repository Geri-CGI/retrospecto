package com.cgi.retrospecto.backend.chat.controller;

import com.cgi.retrospecto.backend.chat.domain.ChatMessage;
import com.cgi.retrospecto.backend.chat.service.ChatHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    @Autowired
    private ChatHandler chatHandler;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chat/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatHandler.sendMessageReceived(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/chat/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        return chatHandler.addUserReceived(chatMessage, headerAccessor);
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/chat/messages", method = RequestMethod.GET)
    public List<ChatMessage> getChatMessages() {
        return chatHandler.getChatMessages();
    }
}
