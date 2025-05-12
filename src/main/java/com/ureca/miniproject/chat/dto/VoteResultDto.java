package com.ureca.miniproject.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteResultDto {
    private String target; 
    private boolean decided; 
    private String type;
}
