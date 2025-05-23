package com.ureca.miniproject.chat.controller;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.GameState;
import com.ureca.miniproject.chat.dto.VoteResultDto;
import com.ureca.miniproject.chat.service.StateManager;
import com.ureca.miniproject.chat.service.VoteService;

import lombok.RequiredArgsConstructor;
@Controller
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final StateManager stateManager;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.vote/{roomId}")
    public void vote(@DestinationVariable("roomId") String roomId, @Payload ChatMessage message) {
        if (stateManager.getGameState(roomId) == GameState.END) return;

        int participantCount = message.getParticipants().size();
        participantCount-=stateManager.getDeadUsers(roomId).size();
        String sender = message.getSender();
        VoteResultDto resultDto = voteService.recordVote(roomId, message.getMessage(), participantCount);

        ChatMessage resultMessage = new ChatMessage();
        resultMessage.setRoomId(roomId);
        resultMessage.setSender("SYSTEM");
        resultMessage.setType(ChatMessage.MessageType.SYSTEM);
        resultMessage.setParticipants(message.getParticipants());
        resultMessage.setDeadUsers(stateManager.getDeadUsers(roomId));
        resultMessage.setId(UUID.randomUUID().toString());

        boolean shouldStartNight = false;

        if (resultDto == null) {
        	
            if (voteService.isVoteComplete(roomId, participantCount)) {
                resultDto = voteService.getPendingResult(roomId);
                int votedCount = voteService.getTotalVotes(roomId);
                if (resultDto != null && resultDto.isDecided()) {
                	
                	resultMessage.setMessage("(" + votedCount + "/" + participantCount + ")명이 투표하였습니다.");
                	messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
                	stateManager.saveChat(roomId, resultMessage);

                	ChatMessage majorityMsg = new ChatMessage();
                	majorityMsg.setRoomId(roomId);
                	majorityMsg.setSender("SYSTEM");
                	majorityMsg.setType(ChatMessage.MessageType.SYSTEM);
                	majorityMsg.setMessage(resultDto.getTarget() + "님이 과반수 득표로 지목되었습니다.");
                	majorityMsg.setParticipants(message.getParticipants());
                	majorityMsg.setDeadUsers(stateManager.getDeadUsers(roomId));
                	majorityMsg.setId(UUID.randomUUID().toString());
                	messagingTemplate.convertAndSend("/topic/chat/" + roomId, majorityMsg);
                	stateManager.saveChat(roomId, majorityMsg);
                    stateManager.setUserAsDead(roomId, resultDto.getTarget());
                    shouldStartNight = true;
                } else {
                	resultMessage.setMessage("(" + votedCount + "/" + participantCount + ")명이 투표하였습니다.");
                	messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
                	stateManager.saveChat(roomId, resultMessage);
                    resultMessage.setMessage("이번 투표는 무효 처리되었습니다.");
                    shouldStartNight = true;
                }

                messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
                stateManager.saveChat(roomId, resultMessage);
                voteService.clearVotes(roomId);
            } else {
            	int votedCount = voteService.getTotalVotes(roomId);
                resultMessage.setMessage("(" + votedCount + "/" + participantCount + ")명이 투표하였습니다.");
                messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
                stateManager.saveChat(roomId, resultMessage);
            }
        } else {
            int votedCount = voteService.getTotalVotes(roomId);
			resultMessage.setMessage("(" + votedCount + "/" + participantCount + ")명이 투표하였습니다.");
			messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
			stateManager.saveChat(roomId, resultMessage);
        }

        if (shouldStartNight) {
            try {
                Thread.sleep(300);
                stateManager.checkAndEndGame(roomId);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

