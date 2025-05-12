package com.ureca.miniproject.friend.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class InviteAlreadyExistException extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BaseCode baseCode;

    public InviteAlreadyExistException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
