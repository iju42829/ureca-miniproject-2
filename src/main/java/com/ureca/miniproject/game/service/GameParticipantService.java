package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.service.response.GameRoomDetailResponse;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;


public interface GameParticipantService {
    Long createParticipant(Long roomId, MyUserDetails myUserDetails);
    GameRoomDetailResponse getGameRoomDetail(Long roomId, MyUserDetails myUserDetails);
    ParticipantCheckResponse checkParticipant(MyUserDetails myUserDetails);
}
