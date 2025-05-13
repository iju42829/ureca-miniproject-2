package com.ureca.miniproject.chat.tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class ChatRoomUserTool {
    private final Map<String, Set<String>> roomUsers = new ConcurrentHashMap<>();

    public void addUser(String roomId, String username) {
        roomUsers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(username);
    }

    public void removeUser(String roomId, String username) {
        Set<String> users = roomUsers.get(roomId);
        if (users != null) {
            users.remove(username);
            if (users.isEmpty()) {
                roomUsers.remove(roomId);
            }
        }
    }

    public List<String> getUsers(String roomId) {
        return new ArrayList<>(roomUsers.getOrDefault(roomId, Collections.emptySet()));
    }
    public boolean isUserInRoom(String roomId, String username) {
        return roomUsers.getOrDefault(roomId, Collections.emptySet()).contains(username);
    }

}
