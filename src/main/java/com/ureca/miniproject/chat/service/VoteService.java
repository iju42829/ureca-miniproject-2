package com.ureca.miniproject.chat.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ureca.miniproject.chat.dto.VoteResultDto;

@Service
public class VoteService {

    private final Map<String, Map<String, Integer>> voteCounts = new ConcurrentHashMap<>();

    private final Map<String, Integer> totalVotes = new ConcurrentHashMap<>();

    public VoteResultDto recordVote(String roomId, String votedUser, int participantCount) {
        voteCounts.putIfAbsent(roomId, new ConcurrentHashMap<>());
        totalVotes.put(roomId, totalVotes.getOrDefault(roomId, 0) + 1);
        Map<String, Integer> roomVotes = voteCounts.get(roomId);
        
        roomVotes.put(votedUser, roomVotes.getOrDefault(votedUser, 0) + 1);

        int majority = participantCount / 2 + 1;

        for (Map.Entry<String, Integer> entry : roomVotes.entrySet()) {
            if (entry.getValue() >= majority) {
            	//TODO: DB에 사망처리 코드 넣기
                return new VoteResultDto(entry.getKey(), true, "VOTE_RESULT");
            }
        }

        return new VoteResultDto(null, false, "VOTE_RESULT");
    }

    public void clearVotes(String roomId) {
        voteCounts.remove(roomId);
        totalVotes.remove(roomId);
    }
}

