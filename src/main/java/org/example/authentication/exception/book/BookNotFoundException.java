package org.example.authentication.exception.book;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BookNotFoundException extends RuntimeException {
    private final String ISBN;

    public BookNotFoundException(String ISBN) {
        super(MessageFormat.format("Book with ISBN {0} not found.", ISBN));
        this.ISBN = ISBN;
    }
}
