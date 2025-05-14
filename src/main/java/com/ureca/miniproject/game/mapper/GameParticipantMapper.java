package com.ureca.miniproject.game.mapper;

import com.ureca.miniproject.game.entity.GameParticipant;
import com.ureca.miniproject.game.service.response.DetailGameParticipantResponse;
import com.ureca.miniproject.game.service.response.GameParticipantResponse;

public interface GameParticipantMapper {
    GameParticipantResponse toGameParticipantResponse(GameParticipant gameParticipant);
    DetailGameParticipantResponse toDetailGameParticipantResponse(GameParticipant gameParticipant);
}
