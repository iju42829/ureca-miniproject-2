package com.ureca.miniproject.chat.dto;


import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    private String roomId;            
    private String sender;            
    private String message;         
    private MessageType type;         
    private Long startTime;
    private List<String> participants;
    private String id = UUID.randomUUID().toString();

    public enum MessageType {
        ENTER, TALK, LEAVE, VOTE, END_TIME, START_TIME
    }

}
