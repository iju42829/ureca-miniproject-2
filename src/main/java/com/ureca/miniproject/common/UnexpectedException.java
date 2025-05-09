package com.ureca.miniproject.common;

import lombok.Getter;

@Getter
public class UnexpectedException extends RuntimeException{
	private final BaseCode baseCode;

    public UnexpectedException(BaseCode baseCode) {
        this.baseCode = baseCode;
    }
}
