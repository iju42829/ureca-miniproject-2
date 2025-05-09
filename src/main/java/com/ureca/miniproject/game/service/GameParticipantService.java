package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.service.response.ListGameParticipantResponse;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;

public interface GameParticipantService {
    Long createParticipant(Long roomId, MyUserDetails myUserDetails);
    ListGameParticipantResponse listGameParticipant(Long roomId);
    ParticipantCheckResponse checkParticipant(MyUserDetails myUserDetails);
}
