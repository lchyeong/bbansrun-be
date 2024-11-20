package com.bbansrun.project1.global.util;

import com.bbansrun.project1.global.exception.ApiException;
import com.bbansrun.project1.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class HeaderUtil {

    public static HttpHeaders setTokenInHeader(String token) {
        if (!StringUtils.hasText(token)) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        if (request == null) {
            throw new ApiException(ErrorCode.INVALID_INPUT_VALUE);
        }
        String authorizationHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authorizationHeader)) {
            throw new ApiException(ErrorCode.MISSING_PARAMETER);
        }

        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new ApiException(ErrorCode.INVALID_TYPE_VALUE);
    }
}
