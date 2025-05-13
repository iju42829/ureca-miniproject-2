package com.ureca.miniproject.chat.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.service.StateManager;
import com.ureca.miniproject.chat.tool.ChatRoomUserTool;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomUserTool userRepo;
    private final StateManager stateManager;

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage send(@Payload ChatMessage message, @DestinationVariable("roomId") String roomId, Principal principal) {

        String username = principal.getName();
        message.setSender(username);

        switch (message.getType()) {
            case ENTER -> {
                userRepo.addUser(roomId, username);
                message.setMessage(username + "님이 입장하셨습니다.");
            }
            case LEAVE -> {
                userRepo.removeUser(roomId, username);
                message.setMessage(username + "님이 퇴장하셨습니다.");
            }
            case START_TIME -> {
                message.setParticipants(userRepo.getUsers(roomId));
                stateManager.startDebate(roomId, message);
                return null;
            }
            case END_TIME -> {
                stateManager.endDebate(roomId, message);
                return null;
            }
        }

        message.setParticipants(userRepo.getUsers(roomId));
        stateManager.saveChat(roomId, message);
        System.out.println(message);
        return message;
    }
}
