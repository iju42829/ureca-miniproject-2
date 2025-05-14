package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.service.response.DetailGameParticipantResponse;
import com.ureca.miniproject.game.service.response.ParticipantCheckResponse;

import java.util.List;


public interface GameParticipantService {
    Long createParticipant(Long roomId, MyUserDetails myUserDetails);
    ParticipantCheckResponse checkParticipant(MyUserDetails myUserDetails);
    List<DetailGameParticipantResponse> getDetailGameParticipant(Long roomId);
}
