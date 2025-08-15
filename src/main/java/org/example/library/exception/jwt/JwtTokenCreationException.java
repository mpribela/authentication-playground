package org.example.library.exception.jwt;

public class JwtTokenCreationException extends RuntimeException {
    public JwtTokenCreationException(String message) {
        super(message);
    }

    public JwtTokenCreationException(Throwable cause) {
        super(cause);
    }
}
