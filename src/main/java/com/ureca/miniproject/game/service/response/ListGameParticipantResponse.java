package com.ureca.miniproject.game.service.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListGameParticipantResponse {

    private List<GameParticipantResponse> participantResponseList;
}
