package com.ureca.miniproject.game.mapper;

import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.service.response.GameRoomResponse;

public interface GameRoomMapper {
    GameRoomResponse toGameRoomResponse(GameRoom gameRoom);
}
