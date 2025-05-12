package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

@Getter
public class GameRoomCapacityExceededException extends IllegalStateException {
    private final BaseCode baseCode;


    public GameRoomCapacityExceededException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
