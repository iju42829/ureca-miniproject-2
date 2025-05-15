package com.ureca.miniproject.game.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

@Getter
public class NotEnoughParticipantsException extends IllegalStateException {
    private final BaseCode baseCode;

    public NotEnoughParticipantsException(BaseCode baseCode) {
        super(baseCode.getMessage());
        this.baseCode = baseCode;
    }
}
