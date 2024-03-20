package org.example.authentication.exception;

public class JwtTokenCreationException extends RuntimeException {
    public JwtTokenCreationException(Throwable cause) {
        super(cause);
    }
}
