package com.ureca.miniproject.user.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class UserNotFoundException extends NoSuchElementException {
    private final BaseCode baseCode;

    public UserNotFoundException(BaseCode baseCode) {
        super(baseCode.getMessage());
        this.baseCode = baseCode;
    }
}
