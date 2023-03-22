package com.cgi.retrospecto.backend.chat.service;

import com.cgi.retrospecto.backend.chat.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatHandler {
    @Autowired
    private ChatMessageKeeper chatMessageKeeper;

    public ChatMessage sendMessageReceived(ChatMessage chatMessage) {
        chatMessage.setLocalDateTime(LocalDateTime.now());
        chatMessageKeeper.addMessage(chatMessage);
        return chatMessage;
    }

    public ChatMessage addUserReceived(ChatMessage chatMessage,
                                       SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setContent(chatMessage.getSender() + " joined!");
        chatMessage.setLocalDateTime(LocalDateTime.now());
        chatMessageKeeper.addMessage(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> getChatMessages() {
        return ChatMessageKeeper.getChatMessages();
    }
}
