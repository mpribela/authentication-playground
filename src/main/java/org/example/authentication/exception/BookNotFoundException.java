package org.example.authentication.exception;

import lombok.Getter;

@Getter
public class BookNotFoundException extends RuntimeException {
    private final String id;

    public BookNotFoundException(String id) {
        super();
        this.id = id;
    }
}
