package com.ureca.miniproject.common;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ureca.miniproject.common.BaseCode.*;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @GetMapping
    public ResponseEntity<ApiResponse<?>> checkStatus() {
        return ResponseEntity
                .ok(ApiResponse.ok(STATUS_OK));
    }

    @GetMapping("/auth")
    public ResponseEntity<ApiResponse<?>> checkStatus(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(BaseCode.STATUS_UNAUTHORIZED.getStatus())
                    .body(ApiResponse.of(BaseCode.STATUS_UNAUTHORIZED, null));
        }

        return ResponseEntity.ok(ApiResponse.ok(STATUS_AUTHENTICATED));
    }
}
