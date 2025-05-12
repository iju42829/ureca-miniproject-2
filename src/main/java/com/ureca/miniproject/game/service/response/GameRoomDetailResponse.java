package com.ureca.miniproject.game.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter @Setter
public class GameRoomDetailResponse {

    private String title;
    private Integer maxPlayer;
    private Integer currentPlayer;
    private Boolean isHost;
    private List<GameParticipantResponse> participantResponseList;
}
