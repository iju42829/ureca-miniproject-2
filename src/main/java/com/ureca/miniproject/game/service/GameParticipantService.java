package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;

public interface GameParticipantService {
    Long createParticipant(Long roomId, MyUserDetails myUserDetails);
}
