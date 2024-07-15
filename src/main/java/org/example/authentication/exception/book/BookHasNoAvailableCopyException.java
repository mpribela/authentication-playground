package org.example.authentication.exception.book;

import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class BookHasNoAvailableCopyException extends RuntimeException {
    private final String ISBN;

    public BookHasNoAvailableCopyException(String ISBN) {
        super(MessageFormat.format("Book with ISBN {0} has no available copy.", ISBN));
        this.ISBN = ISBN;
    }
}
