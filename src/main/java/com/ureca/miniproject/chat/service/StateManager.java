package com.ureca.miniproject.chat.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.GameState;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StateManager {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, GameState> roomStates = new ConcurrentHashMap<>();
    private final Map<String, Long> debateStartTimes = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long DEBATE_DURATION = 120_000;

    public void startDebate(String roomId, ChatMessage baseMessage) {
        long startTime = System.currentTimeMillis();
        roomStates.put(roomId, GameState.DEBATING);
        debateStartTimes.put(roomId, startTime);
        baseMessage.setStartTime(startTime);
        baseMessage.setMessage("토론을 시작합니다.");
        baseMessage.setType(ChatMessage.MessageType.START_TIME);

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, baseMessage);

        scheduler.scheduleAtFixedRate(() -> {
            if (roomStates.get(roomId) != GameState.DEBATING) return;

            long remaining = getRemainingTime(roomId);
            if (remaining <= 0) {
                endDebate(roomId, baseMessage);
            } else {
                messagingTemplate.convertAndSend("/topic/chat/" + roomId, new ChatMessage(
                        roomId,
                        "SYSTEM",
                        "남은 시간: " + remaining / 1000 + "초",
                        ChatMessage.MessageType.TALK,
                        null,
                        baseMessage.getParticipants()
                ));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void endDebate(String roomId, ChatMessage baseMessage) {
        roomStates.put(roomId, GameState.VOTING);
        debateStartTimes.remove(roomId);

        ChatMessage endMessage = new ChatMessage();
        endMessage.setRoomId(roomId);
        endMessage.setSender("SYSTEM");
        endMessage.setMessage("시간이 종료되었습니다.");
        endMessage.setType(ChatMessage.MessageType.END_TIME);
        endMessage.setParticipants(baseMessage.getParticipants());

        messagingTemplate.convertAndSend("/topic/chat/" + roomId, endMessage);
    }

    public long getRemainingTime(String roomId) {
        Long start = debateStartTimes.get(roomId);
        if (start == null) return 0;
        return Math.max(0, DEBATE_DURATION - (System.currentTimeMillis() - start));
    }

    public GameState getGameState(String roomId) {
        return roomStates.getOrDefault(roomId, GameState.WAITING);
    }

	public Long getStartTime(String roomId) {
		return debateStartTimes.get(roomId);
	}
}
