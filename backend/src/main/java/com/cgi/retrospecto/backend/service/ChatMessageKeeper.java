package com.cgi.retrospecto.backend.service;

import com.cgi.retrospecto.backend.domain.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class ChatMessageKeeper {

    private static final LinkedList<ChatMessage> chatMessages = new LinkedList<>();

    public ChatMessageKeeper() {
    }

    public void addMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
    }

    public static LinkedList<ChatMessage> getChatMessages() {
        return chatMessages;
    }
}
