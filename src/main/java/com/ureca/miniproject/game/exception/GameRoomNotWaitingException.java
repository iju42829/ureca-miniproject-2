package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

@Getter
public class GameRoomNotWaitingException extends IllegalStateException{
    private final BaseCode baseCode;

    public GameRoomNotWaitingException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
