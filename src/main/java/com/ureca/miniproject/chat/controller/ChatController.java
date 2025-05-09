package com.ureca.miniproject.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;

import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.tool.ChatRoomUserTool;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {


    private final ChatRoomUserTool userRepo;

    @MessageMapping("/chat.send/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage send(@Payload ChatMessage message, @DestinationVariable("roomId") String roomId) {
    	System.out.println(message+roomId+"send");
        if (message.getType() == ChatMessage.MessageType.ENTER) {
            userRepo.addUser(roomId, message.getSender());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
            System.out.println(message.getSender()+"입장");
        } 
        else if (message.getType() == ChatMessage.MessageType.LEAVE) {
            userRepo.removeUser(roomId, message.getSender());
            message.setMessage(message.getSender() + "님이 퇴장하셨습니다.");
            System.out.println(message.getSender()+"퇴장");
        }
        else if (message.getType() == ChatMessage.MessageType.END_TIME) {
            message.setMessage("시간이 종료되었습니다.");
            message.setStartTime(System.currentTimeMillis());
        }
        else if (message.getType() == ChatMessage.MessageType.START_TIME) {
            message.setMessage("토론을 시작합니다.");
            message.setStartTime(System.currentTimeMillis()); 
        }


        message.setParticipants(userRepo.getUsers(roomId));
        return message;
    }

}
