package org.example.authentication.exception;

public class JwtTokenCreationException extends RuntimeException {
    public JwtTokenCreationException(String message) {
        super(message);
    }

    public JwtTokenCreationException(Throwable cause) {
        super(cause);
    }
}
