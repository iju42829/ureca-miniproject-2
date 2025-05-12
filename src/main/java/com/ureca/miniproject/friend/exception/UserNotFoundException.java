package com.ureca.miniproject.friend.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class UserNotFoundException extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BaseCode baseCode;

    public UserNotFoundException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
