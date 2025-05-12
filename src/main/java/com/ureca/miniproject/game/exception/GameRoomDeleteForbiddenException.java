package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

@Getter
public class GameRoomDeleteForbiddenException extends IllegalStateException {
    private final BaseCode baseCode;

    public GameRoomDeleteForbiddenException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
