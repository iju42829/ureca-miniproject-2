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
    private final Map<String, Boolean> alreadyDecided = new ConcurrentHashMap<>();
    private final Map<String, Integer> totalVotes = new ConcurrentHashMap<>();

    public VoteResultDto recordVote(String roomId, String votedUser, int participantCount) {
    	if (alreadyDecided.getOrDefault(roomId, false)) {
            return null;
        }
        voteCounts.putIfAbsent(roomId, new ConcurrentHashMap<>());
        totalVotes.put(roomId, totalVotes.getOrDefault(roomId, 0) + 1);
        Map<String, Integer> roomVotes = voteCounts.get(roomId);
        
        roomVotes.put(votedUser, roomVotes.getOrDefault(votedUser, 0) + 1);

        int majority = participantCount / 2 + 1;

        for (Map.Entry<String, Integer> entry : roomVotes.entrySet()) {
        	if (entry.getValue() >= majority) {
        	    stateManager.setUserAsDead(roomId, entry.getKey()); 
        	    alreadyDecided.put(roomId, true);
        	    return new VoteResultDto(entry.getKey(), true, "VOTE_RESULT");
        	}

        }

        return null;
    }

    public void clearVotes(String roomId) {
        voteCounts.remove(roomId);
        totalVotes.remove(roomId);
        alreadyDecided.remove(roomId);
    }
}

