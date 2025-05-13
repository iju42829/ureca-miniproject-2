package com.ureca.miniproject.friend.exception;

import com.ureca.miniproject.common.BaseCode;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
public class InviteSelfException extends IllegalArgumentException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final BaseCode baseCode;

    public InviteSelfException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
