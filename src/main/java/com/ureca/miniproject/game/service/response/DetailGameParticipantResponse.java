package com.ureca.miniproject.game.service.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DetailGameParticipantResponse {
    private Long id;
    private String name;
    private String status;
    private Boolean isAlive;
    private String role;
}
