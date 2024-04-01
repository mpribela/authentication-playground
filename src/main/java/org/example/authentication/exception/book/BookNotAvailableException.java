package org.example.authentication.exception.book;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BookNotAvailableException extends RuntimeException {
    private final String ISBN;

    public BookNotAvailableException(String ISBN) {
        super(MessageFormat.format("Book with ISBN {0} is not available.", ISBN));
        this.ISBN = ISBN;
    }
}
