package com.ureca.miniproject.chat.dto;

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

    public enum MessageType {
        ENTER, TALK, LEAVE
    }
}
