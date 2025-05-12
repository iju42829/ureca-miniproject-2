package com.ureca.miniproject.game.service;

import com.ureca.miniproject.game.controller.request.EndGameRequest;

public interface GameService {
    void startGame(Long roomId);
    void endGame(Long roomId, EndGameRequest endGameRequest);
    void updateDeathStatus(Long roomId, String username);
}
