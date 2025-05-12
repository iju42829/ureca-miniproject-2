package com.ureca.miniproject.game.service.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameResultResponse {
    private String gameName;
    private String hostUsername;
    private LocalDateTime endTime;
    private String resultStatus;
}
