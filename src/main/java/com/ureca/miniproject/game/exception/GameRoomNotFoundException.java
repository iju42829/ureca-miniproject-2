package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class GameRoomNotFoundException extends NoSuchElementException {
    private final BaseCode baseCode;

    public GameRoomNotFoundException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
