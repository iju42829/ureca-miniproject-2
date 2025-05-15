package com.ureca.miniproject.chat.controller;


import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.GameState;
import com.ureca.miniproject.chat.service.StateManager;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NightController {

    private final StateManager stateManager;
    private final SimpMessagingTemplate messagingTemplate;
    @MessageMapping("/chat.mafia/{roomId}")
    public void mafiaKill(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        if (stateManager.getGameState(roomId) == GameState.END) return;
        String target = message.getMessage(); 
        stateManager.setPendingDeath(roomId, target);
        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setMessage("마피아가 대상을 정했습니다.");
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setDeadUsers(stateManager.getDeadUsers(roomId));
        resultMessage.setId(UUID.randomUUID().toString());

        stateManager.saveChat(roomId, resultMessage);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
        stateManager.setMafiaActed(roomId);
        stateManager.checkNightActions(roomId);

        
    }
    @MessageMapping("/chat.police/{roomId}")
    public void policeCheck(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        String target = message.getMessage();
        String role = stateManager.getUserRole(roomId, target);

        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setMessage(target + "님의 직업은 [" + role + "]입니다.");
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setId(UUID.randomUUID().toString());
        stateManager.setPoliceActed(roomId);
        stateManager.checkNightActions(roomId);
        stateManager.sendPoliceResultToSender(message.getSender(), roomId, resultMessage);
    }
}
