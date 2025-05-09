package com.ureca.miniproject.user.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class UserAlreadyExistException extends IllegalArgumentException {
    private final BaseCode baseCode;

    public UserAlreadyExistException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
