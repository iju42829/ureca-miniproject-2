package com.ureca.miniproject.chat.controller;

import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.VoteResultDto;
import com.ureca.miniproject.chat.service.StateManager;
import com.ureca.miniproject.chat.service.VoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final StateManager stateManager;

    @MessageMapping("/chat.vote/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage vote(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        int participantCount = message.getParticipants().size();
        VoteResultDto resultDto = voteService.recordVote(roomId, message.getMessage(), participantCount);
        if (resultDto == null) {
            return null; 
        }
        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setMessage(
            resultDto.isDecided()
                ? resultDto.getTarget() + "님이 과반수 득표로 지목되었습니다."
                : "투표가 진행 중입니다..."
        );
        resultMessage.setDeadUsers(stateManager.getDeadUsers(roomId));
        resultMessage.setId(UUID.randomUUID().toString());
        System.out.println("투표결과:");
        System.out.println(resultMessage);
        stateManager.saveChat(roomId, resultMessage); 
        return resultMessage;
    }

}
