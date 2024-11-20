package com.bbansrun.project1.global.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // 공통 에러
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "허용되지 않은 메서드입니다"),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "C003", "엔티티를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "서버 내부 오류"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "유효하지 않은 타입 값"),
    HANDLE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "C006", "접근이 거부되었습니다"),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "C007", "지원되지 않는 미디어 타입입니다"),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "C008", "요청 파라미터가 누락되었습니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C009", "인증되지 않음"),


    // 사용자 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    INVALID_USER_PASSWORD(HttpStatus.BAD_REQUEST, "U002", "유효하지 않은 비밀번호입니다"),
    EMAIL_ALREADY_EXISTED(HttpStatus.CONFLICT, "U003", "이메일이 이미 존재합니다."),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "U004", "유효한 이메일 주소를 입력해주세요."),
    MISSING_REQUIRED_FIELDS(HttpStatus.BAD_REQUEST, "U005", "필수 입력 값이 누락되었습니다."),
    TERMS_NOT_ACCEPTED(HttpStatus.FORBIDDEN, "U006", "필수 약관에 동의하지 않았습니다."),
    USERNAME_ALREADY_EXISTED(HttpStatus.CONFLICT, "U007", "사용자명이 이미 존재합니다."),
    INVALID_PROMO_CODE(HttpStatus.BAD_REQUEST, "U008", "유효하지 않은 프로모션 코드입니다."),
    VERIFICATION_FAILED(HttpStatus.BAD_REQUEST, "U009", "인증에 실패했습니다."),
    REGISTRATION_BLOCKED(HttpStatus.FORBIDDEN, "U010", "등록이 제한된 사용자입니다."),

    // 로그인 및 로그아웃 관련 에러
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "L001", "제공된 자격 증명이 유효하지 않습니다"),
    USER_ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "L002", "사용자 계정이 잠겼습니다"),
    USER_ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "L003", "사용자 계정이 비활성화되었습니다"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "L004", "리프레시 토큰이 만료되었습니다"),
    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "L005", "유효하지 않은 리프레시 토큰입니다"),

    // Authorization 관련 에러
    INVALID_HEADER_FORMAT(HttpStatus.BAD_REQUEST, "H001", "Authorization 헤더 형식이 유효하지 않습니다."),
    MISSING_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "H002", "Authorization 헤더가 누락되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatusCode() {
        return status.value();
    }
}
