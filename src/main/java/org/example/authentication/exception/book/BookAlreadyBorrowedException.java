package org.example.authentication.exception.book;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BookAlreadyBorrowedException extends RuntimeException {
    private final String ISBN;
    private final String userId;

    public BookAlreadyBorrowedException(String ISBN, String userId) {
        super(MessageFormat.format("Book with ISBN {0} is already borrowed by user {1}.", ISBN, userId));
        this.ISBN = ISBN;
        this.userId = userId;
    }
}
