package com.ureca.miniproject.friend.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class UserAlreadyExistException3 extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BaseCode baseCode;

    public UserAlreadyExistException3(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
