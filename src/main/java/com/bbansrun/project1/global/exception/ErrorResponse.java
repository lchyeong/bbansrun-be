package com.bbansrun.project1.global.exception;


import org.springframework.http.HttpStatus;

public record ErrorResponse(HttpStatus status, int statusCode, String code) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getStatusCode(),
                errorCode.getCode());
    }
}
