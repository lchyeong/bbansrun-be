package com.bbansrun.project1.global.exception.handler;

import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import com.bbansrun.project1.global.exception.ErrorResponse;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * ApiException을 처리하고 적절한 에러 응답을 반환합니다.
     *
     * @param e 처리할 ApiException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        log.error("오류 발생: {} - {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage());
        return new ResponseEntity<>(ErrorResponse.of(e.getErrorCode()), e.getErrorCode().getStatus());
    }

    /**
     * AuthenticationException을 처리하고 인증 오류에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 AuthenticationException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.error("인증 오류: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.UNAUTHORIZED), ErrorCode.UNAUTHORIZED.getStatus());
    }

    /**
     * HttpRequestMethodNotSupportedException을 처리하고 지원되지 않는 HTTP 메서드에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 HttpRequestMethodNotSupportedException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        log.error("지원되지 않는 요청 메서드: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED),
                ErrorCode.METHOD_NOT_ALLOWED.getStatus());
    }

    /**
     * HttpMediaTypeNotSupportedException을 처리하고 지원되지 않는 미디어 타입에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 HttpMediaTypeNotSupportedException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        log.error("지원되지 않는 미디어 타입: {}", e.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INVALID_TYPE_VALUE),
                ErrorCode.INVALID_TYPE_VALUE.getStatus());
    }

    /**
     * MissingServletRequestParameterException을 처리하고 필수 요청 파라미터 누락에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 MissingServletRequestParameterException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        log.error("필수 요청 파라미터 누락: {}", e.getParameterName());
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.MISSING_PARAMETER),
                ErrorCode.MISSING_PARAMETER.getStatus());
    }

    /**
     * MethodArgumentNotValidException을 처리하고 유효성 검증 실패에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 MethodArgumentNotValidException
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String errorMessage = Optional.ofNullable(fieldError)
                .map(FieldError::getDefaultMessage)
                .orElse("유효성 검증 오류가 발생했습니다.");

        log.error("유효성 검증 실패: {}", errorMessage);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE),
                ErrorCode.INVALID_INPUT_VALUE.getStatus());
    }

    /**
     * 예상치 못한 예외를 처리하고 서버 내부 오류에 대한 에러 응답을 반환합니다.
     *
     * @param e 처리할 Exception
     * @return ErrorResponse와 HTTP 상태 코드를 포함한 ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
        return new ResponseEntity<>(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR),
                ErrorCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
