package com.bbansrun.project1.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 400, "C001", "Invalid input value"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "C002", "Method not allowed"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "C003", "Entity not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "C004", "Internal server error"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, 400, "C005", "Invalid type value"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "C006", "Access is denied"),

    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "U001", "User not found"),
    INVALID_USER_PASSWORD(HttpStatus.BAD_REQUEST, 400, "U002", "Invalid user password"),

    // 추가적인 공통 에러
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, 415, "C007",
        "Media type not supported"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, 400, "C008", "Missing request parameter"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "C009", "Unauthorized");
    
    private final HttpStatus status;
    private final int statusCode;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, int statusCode, String code, String message) {
        this.status = status;
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }
}
