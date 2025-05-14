package com.ureca.miniproject.chat.controller;


import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.service.StateManager;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NightController {

    private final StateManager stateManager;

    @MessageMapping("/chat.mafia/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage mafiaKill(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        String target = message.getMessage(); 
        stateManager.setUserAsDead(roomId, target);

        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setMessage(target + "님이 밤에 살해당했습니다.");
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setDeadUsers(stateManager.getDeadUsers(roomId));
        resultMessage.setId(UUID.randomUUID().toString());
        
        stateManager.saveChat(roomId, resultMessage);
        return resultMessage;
    }
}
