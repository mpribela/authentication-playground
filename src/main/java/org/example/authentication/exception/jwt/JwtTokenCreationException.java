package org.example.authentication.exception.jwt;

public class JwtTokenCreationException extends RuntimeException {
    public JwtTokenCreationException(String message) {
        super(message);
    }

    public JwtTokenCreationException(Throwable cause) {
        super(cause);
    }
}
