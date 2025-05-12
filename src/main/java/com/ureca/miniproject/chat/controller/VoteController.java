package com.ureca.miniproject.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.VoteResultDto;
import com.ureca.miniproject.chat.service.VoteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @MessageMapping("/chat.vote/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public VoteResultDto vote(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        int participantCount = message.getParticipants().size();
        VoteResultDto resultDto = voteService.recordVote(roomId, message.getMessage(), participantCount);
        System.out.println(resultDto.getTarget());
        return resultDto;
    }
}
