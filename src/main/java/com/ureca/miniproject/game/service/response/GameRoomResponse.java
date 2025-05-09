package com.ureca.miniproject.game.service.response;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GameRoomResponse {
    private Long roomId;
    private String title;
    private Integer maxPlayer;
    private Integer currentPlayer;
    private String status;
}
