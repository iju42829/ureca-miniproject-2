package com.ureca.miniproject.game.mapper;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.service.response.GameParticipantResponse;
import org.springframework.stereotype.Component;

@Component
public class GameParticipantMapperImpl implements GameParticipantMapper {

    @Override
    public GameParticipantResponse toGameParticipantResponse(GameParticipant gameParticipant) {
        if (gameParticipant == null) { return null; }

        return GameParticipantResponse.builder()
                .id(gameParticipant.getUser().getId())
                .name(gameParticipant.getUser().getUserName())
                .status(gameParticipant.getStatus().name())
                .isAlive(gameParticipant.getIsAlive())
                .build();
    }
}
