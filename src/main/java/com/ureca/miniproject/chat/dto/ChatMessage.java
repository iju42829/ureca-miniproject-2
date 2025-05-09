package com.ureca.miniproject.chat.dto;

import java.util.List;

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
    private List<String> participants;

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
}
