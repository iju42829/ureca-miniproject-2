package com.ureca.miniproject.chat.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ureca.miniproject.chat.dto.ChatMessage;
import com.ureca.miniproject.chat.dto.GameState;
import com.ureca.miniproject.chat.tool.ChatRoomUserTool;
import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.repository.GameParticipantRepository;
import com.ureca.miniproject.game.service.GameService;
import com.ureca.miniproject.game.service.response.EndStatusResponse;
import com.ureca.miniproject.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StateManager {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomUserTool chatRoomUserTool;
    private final GameService gameService;
    private final GameParticipantRepository gameParticipantRepository;
    private final Map<String, GameState> roomStates = new ConcurrentHashMap<>();
    private final Map<String, Long> debateStartTimes = new ConcurrentHashMap<>();

    private final Map<String, List<ChatMessage>> chatHistories = new ConcurrentHashMap<>();
    private final Map<String, List<String>> deadUsers = new ConcurrentHashMap<>();
    
    private final Map<String, Boolean> mafiaActed = new ConcurrentHashMap<>();
    private final Map<String, Boolean> policeActed = new ConcurrentHashMap<>();
    private final Map<String, String> pendingDeaths = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final long DEBATE_DURATION = 120_000;

    public void startDebate(String roomId, ChatMessage baseMessage) {
        long startTime = System.currentTimeMillis();
        roomStates.put(roomId, GameState.DEBATING);
        debateStartTimes.put(roomId, startTime);

        baseMessage.setStartTime(startTime);
        baseMessage.setMessage("토론을 시작합니다.");
        baseMessage.setType(ChatMessage.MessageType.START_TIME);
        baseMessage.setId(UUID.randomUUID().toString());
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, baseMessage);
        saveChat(roomId, baseMessage); 


        scheduler.scheduleAtFixedRate(() -> {
            if (roomStates.get(roomId) != GameState.DEBATING) return;

            long remaining = getRemainingTime(roomId);
            if (remaining <= 0) {
                endDebate(roomId, baseMessage);
            } else {

                ChatMessage msg = new ChatMessage();
                msg.setRoomId(roomId);
                msg.setSender("SYSTEM");
                msg.setMessage("남은 시간: " + remaining / 1000 + "초");
                msg.setType(ChatMessage.MessageType.TALK);
                msg.setParticipants(baseMessage.getParticipants());

                messagingTemplate.convertAndSend("/topic/chat/" + roomId, msg);
                saveChat(roomId, msg);
                messagingTemplate.convertAndSend("/topic/chat/" + roomId, new ChatMessage(
                        roomId,
                        "SYSTEM",
                        "남은 시간: " + remaining / 1000 + "초",
                        ChatMessage.MessageType.TALK,
                        null,
                        baseMessage.getParticipants(),
                        UUID.randomUUID().toString(),
                        getDeadUsers(roomId)
                        
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
        endMessage.setDeadUsers(getDeadUsers(roomId));
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, endMessage);
        saveChat(roomId, endMessage);
        
    }
    public void startNight(String roomId) {
    	System.out.println("밤 시작");
    	if (getGameState(roomId) == GameState.END) return;
    	else roomStates.put(roomId, GameState.NIGHT);
    	mafiaActed.put(roomId, false);
        policeActed.put(roomId, false);
        ChatMessage nightMsg = new ChatMessage();
        nightMsg.setRoomId(roomId);
        nightMsg.setSender("SYSTEM");
        nightMsg.setMessage("밤이 되었습니다. 마피아는 시민을 제거하세요.\n경찰은 한 사람의 직업을 알 수 있습니다.");
        nightMsg.setType(ChatMessage.MessageType.SYSTEM);
        nightMsg.setParticipants(chatRoomUserTool.getUsers(roomId));
        nightMsg.setId(UUID.randomUUID().toString());
        nightMsg.setDeadUsers(getDeadUsers(roomId));
        
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, nightMsg);
        saveChat(roomId, nightMsg);
        scheduler.schedule(() -> {
            List<String> allMafiaUsernames = gameParticipantRepository.findMafiaUsernamesByRoomId(Long.parseLong(roomId));
            List<String> allPoliceUsernames = gameParticipantRepository.findPoliceUsernamesByRoomId(Long.parseLong(roomId));
            List<String> deadUsers = getDeadUsers(roomId);

            for (String mafia : allMafiaUsernames) {
                if (!deadUsers.contains(mafia)) {
                    System.out.println("마피아 메시지 전송 대상: " + mafia);
                    messagingTemplate.convertAndSendToUser(mafia, "/queue/night/" + roomId, nightMsg);
                }
            }

            for (String police : allPoliceUsernames) {
                if (!deadUsers.contains(police)) {
                    System.out.println("경찰 메시지 전송 대상: " + police);
                    messagingTemplate.convertAndSendToUser(police, "/queue/police/" + roomId, nightMsg);
                }
            }
        }, 2, TimeUnit.SECONDS);
    }

    public void checkAndEndGame(String roomId) {
        EndStatusResponse endStatus = gameService.isGameEnded(Long.parseLong(roomId));
        System.out.println("[CALL] checkAndEndGame() 실행됨 - 현재 상태: " + getGameState(roomId));
        if (!"NONE".equals(endStatus.getEndStatus())) {
            roomStates.put(roomId, GameState.END);

            ChatMessage endMsg = new ChatMessage();
            endMsg.setRoomId(roomId);
            endMsg.setSender("SYSTEM");
            endMsg.setType(ChatMessage.MessageType.SYSTEM);
            endMsg.setParticipants(chatRoomUserTool.getUsers(roomId));
            endMsg.setDeadUsers(getDeadUsers(roomId));
            endMsg.setId(UUID.randomUUID().toString());

            if ("MAFIA".equals(endStatus.getEndStatus())) {
                endMsg.setMessage("마피아 승리!");
            } else if ("CITIZEN".equals(endStatus.getEndStatus())) {
                endMsg.setMessage("시민 승리!");
            }

            messagingTemplate.convertAndSend("/topic/chat/" + roomId, endMsg);
            saveChat(roomId, endMsg);

        }
        else
        {
        	GameState current = getGameState(roomId);
            if (current == GameState.VOTING) {
                startNight(roomId); 
            } else if (current == GameState.NIGHT) {
                ChatMessage msg = new ChatMessage();
                msg.setRoomId(roomId);
                msg.setSender("SYSTEM");
                msg.setParticipants(chatRoomUserTool.getUsers(roomId));
                startDebate(roomId, msg);
            }
        }
        
    }
    public void saveChat(String roomId, ChatMessage message) {
    	if (message.getDeadUsers() == null) {
            message.setDeadUsers(getDeadUsers(roomId));
        }
        chatHistories.computeIfAbsent(roomId, k -> new ArrayList<>()).add(message);
    }

    public List<ChatMessage> getChatHistory(String roomId) {
        return chatHistories.getOrDefault(roomId, Collections.emptyList());
    }

    public long getRemainingTime(String roomId) {
        Long start = debateStartTimes.get(roomId);
        if (start == null) return 0;
        return Math.max(0, DEBATE_DURATION - (System.currentTimeMillis() - start));
    }


    public Long getStartTime(String roomId) {
        return debateStartTimes.get(roomId);
    }

    public GameState getGameState(String roomId) {
        return roomStates.getOrDefault(roomId, GameState.WAITING);
    }
    

    
    public void setUserAsDead(String roomId, String username) {
        deadUsers.computeIfAbsent(roomId, k -> new ArrayList<>()).add(username);

        try {
            Long roomIdLong = Long.parseLong(roomId);
            Optional<GameParticipant> optional = gameParticipantRepository.findAllByGameRoom_Id(roomIdLong)
                .stream()
                .filter(p -> p.getUser().getUserName().equals(username))
                .findFirst();

            optional.ifPresent(participant -> {
                participant.setIsAlive(false);
                gameParticipantRepository.save(participant);
            });

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    
    public List<String> getDeadUsers(String roomId) {
        return deadUsers.getOrDefault(roomId, new ArrayList<>());
    }
    public String getUserRole(String roomId, String username) {
        return gameParticipantRepository.findAllByGameRoom_Id(Long.parseLong(roomId)).stream()
            .filter(p -> p.getUser().getUserName().equals(username))
            .findFirst()
            .map(p -> p.getRole().name())
            .orElse("UNKNOWN");
    }

    public void sendPoliceResultToSender(String sender, String roomId, ChatMessage message) {
        messagingTemplate.convertAndSendToUser(sender, "/queue/police/" + roomId, message);
    }

    public void setMafiaActed(String roomId) {
        mafiaActed.put(roomId, true);
        System.out.println("[SET] mafiaActed = true for room " + roomId);
    }

    public void setPoliceActed(String roomId) {
        policeActed.put(roomId, true);
        
    }
    public void checkNightActions(String roomId) {
    	
        boolean mafiaDone = mafiaActed.getOrDefault(roomId, false);
        boolean policeDone = policeActed.getOrDefault(roomId, false);
        
        List<String> alivePolice = gameParticipantRepository.findAlivePoliceUsernamesByRoomId(Long.parseLong(roomId));
        System.out.println(alivePolice);
        if (alivePolice.isEmpty()) {
            policeDone = true;
            policeActed.put(roomId, true);
        }
        System.out.println("mafia:"+mafiaDone);
        System.out.println("police:"+policeDone);
        if (mafiaDone && policeDone) {
            mafiaActed.remove(roomId);
            policeActed.remove(roomId);
            if (pendingDeaths.containsKey(roomId)) {
                String victim = pendingDeaths.remove(roomId);
                setUserAsDead(roomId, victim);

                scheduler.schedule(() -> {
                    ChatMessage resultMessage = new ChatMessage();
                    resultMessage.setRoomId(roomId);
                    resultMessage.setSender("SYSTEM");
                    resultMessage.setType(ChatMessage.MessageType.SYSTEM);
                    resultMessage.setMessage(victim + "님이 밤에 살해당했습니다.");
                    resultMessage.setParticipants(chatRoomUserTool.getUsers(roomId));
                    resultMessage.setDeadUsers(getDeadUsers(roomId));
                    resultMessage.setId(UUID.randomUUID().toString());

                    saveChat(roomId, resultMessage);
                    messagingTemplate.convertAndSend("/topic/chat/" + roomId, resultMessage);
                    checkAndEndGame(roomId);
                }, 2, TimeUnit.SECONDS);
            }
//            ChatMessage msg = new ChatMessage();
//            msg.setRoomId(roomId);
//            msg.setSender("SYSTEM");
//            msg.setParticipants(chatRoomUserTool.getUsers(roomId));
            
            
        }
    }
    public void setPendingDeath(String roomId, String username) {
        pendingDeaths.put(roomId, username);
    }

    public String getPendingDeath(String roomId) {
        return pendingDeaths.get(roomId);
    }

    public void clearPendingDeath(String roomId) {
        pendingDeaths.remove(roomId);
    }

}
