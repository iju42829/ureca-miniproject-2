package com.ureca.miniproject.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@DestinationVariable("roomId") String roomId, ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
    }
}
