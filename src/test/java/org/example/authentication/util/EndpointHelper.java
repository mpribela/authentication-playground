package org.example.authentication.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.text.MessageFormat;

public class EndpointHelper {
    public static <T> HttpEntity<T> createHttpEntity(String token, T body) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(body, headers);
    }

    public static String createUrl(String url, String... params) {
        return MessageFormat.format(url, params);
    }
}
