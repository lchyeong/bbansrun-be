package com.bbansrun.project1.domain.auth.util;

import org.springframework.http.HttpHeaders;

public class HeaderUtil {

    public static HttpHeaders setTokenInHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
