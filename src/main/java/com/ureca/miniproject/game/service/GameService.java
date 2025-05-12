package com.ureca.miniproject.game.service;

public interface GameService {
    void startGame(Long roomId);
    void updateDeathStatus(Long roomId, String username);
}
