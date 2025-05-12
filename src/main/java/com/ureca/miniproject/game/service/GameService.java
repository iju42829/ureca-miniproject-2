package com.ureca.miniproject.game.service;

import com.ureca.miniproject.config.MyUserDetails;
import com.ureca.miniproject.game.controller.request.EndGameRequest;
import com.ureca.miniproject.game.service.response.ListGameResultResponse;

public interface GameService {
    void startGame(Long roomId);
    void endGame(Long roomId, EndGameRequest endGameRequest);
    void updateDeathStatus(Long roomId, String username);
    ListGameResultResponse listGameResult(MyUserDetails myUserDetails);
}
