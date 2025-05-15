package com.ureca.miniproject.chat.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ureca.miniproject.chat.dto.VoteResultDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final StateManager stateManager;

    private final Map<String, Map<String, Integer>> voteCounts = new ConcurrentHashMap<>();
    private final Map<String, Integer> totalVotes = new ConcurrentHashMap<>();
    private final Map<String, VoteResultDto> pendingVoteResults = new ConcurrentHashMap<>();

    public VoteResultDto recordVote(String roomId, String votedUser, int participantCount) {
        voteCounts.putIfAbsent(roomId, new ConcurrentHashMap<>());
        totalVotes.put(roomId, totalVotes.getOrDefault(roomId, 0) + 1);
        Map<String, Integer> roomVotes = voteCounts.get(roomId);

        roomVotes.put(votedUser, roomVotes.getOrDefault(votedUser, 0) + 1);

        System.out.println("[투표 현황] roomId: " + roomId);
        System.out.println("  - 총 투표 수: " + totalVotes.get(roomId));
        System.out.println("  - 개별 득표 수:");
        for (Map.Entry<String, Integer> entry : roomVotes.entrySet()) {
            System.out.println("    > " + entry.getKey() + ": " + entry.getValue());
        }

        int majority = participantCount / 2 + 1;
        for (Map.Entry<String, Integer> entry : roomVotes.entrySet()) {
            if (entry.getValue() >= majority) {
                VoteResultDto result = new VoteResultDto(entry.getKey(), true, "VOTE_RESULT");
                pendingVoteResults.put(roomId, result); 
                return null;
            }
        }

        return new VoteResultDto(null, false, "VOTE_RESULT");

    }

    public boolean isVoteComplete(String roomId, int participantCount) {
        return totalVotes.getOrDefault(roomId, 0) >= participantCount;
    }

    public VoteResultDto getPendingResult(String roomId) {
        return pendingVoteResults.remove(roomId);
    }

    public int getTotalVotes(String roomId) {
        return totalVotes.getOrDefault(roomId, 0);
    }

    public void clearVotes(String roomId) {
        voteCounts.remove(roomId);
        totalVotes.remove(roomId);
        pendingVoteResults.remove(roomId);
    }
}