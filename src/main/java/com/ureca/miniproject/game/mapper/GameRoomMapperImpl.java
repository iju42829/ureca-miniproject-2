package com.ureca.miniproject.game.mapper;

import com.ureca.miniproject.game.entity.GameRoom;
import com.ureca.miniproject.game.service.response.GameRoomResponse;
import org.springframework.stereotype.Component;

@Component
public class GameRoomMapperImpl implements GameRoomMapper {
    @Override
    public GameRoomResponse toGameRoomResponse(GameRoom gameRoom) {
        if (gameRoom == null) {
            return null;
        }

        return GameRoomResponse.builder()
                .roomId(gameRoom.getId())
                .title(gameRoom.getTitle())
                .maxPlayer(gameRoom.getMaxPlayer())
                .currentPlayer(gameRoom.getCurrentPlayer())
                .status(gameRoom.getRoomStatus().name())
                .build();
    }
}
