package com.ureca.miniproject.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final HttpStatus status;
    private final String message;
    private final T data;
    private final LocalDateTime timestamp;

    private ApiResponse(BaseCode baseCode, T data) {
        this.code = baseCode.getCode();
        this.status = baseCode.getStatus();
        this.message = baseCode.getMessage();
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> of(BaseCode code, T data) {
        return new ApiResponse<>(code, data);
    }

    public static <T> ApiResponse<T> ok(BaseCode code) {
        return new ApiResponse<>(code, null);
    }
}
