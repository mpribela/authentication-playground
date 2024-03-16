package org.example.authentication.exception;

import lombok.Getter;

@Getter
public class BookAlreadyBorrowedException extends RuntimeException {
    private final String id;

    public BookAlreadyBorrowedException(String id) {
        super();
        this.id = id;
    }
}
