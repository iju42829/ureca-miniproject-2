package com.ureca.miniproject.game.service.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class ListGameRoomResponse {

    private List<GameRoomResponse> rooms;
}
