package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

@Getter
public class AlreadyJoinedException extends IllegalStateException {
    private final BaseCode baseCode;

    public AlreadyJoinedException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
