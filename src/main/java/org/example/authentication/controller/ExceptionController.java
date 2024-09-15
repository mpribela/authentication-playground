package org.example.authentication.controller;

import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.ErrorDto;
import org.example.authentication.exception.book.BookAlreadyBorrowedException;
import org.example.authentication.exception.book.BookHasNoAvailableCopyException;
import org.example.authentication.exception.book.BookNotFoundException;
import org.example.authentication.exception.jwt.JwtTokenCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;

//todo translatable messages
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleBookNotFoundException(BookNotFoundException exception) {
        String errorMessage = MessageFormat.format("Book with ISBN {0} not found.", exception.getISBN());
        return ErrorDto.builder().message(errorMessage).build();
    }

    @ExceptionHandler(BookHasNoAvailableCopyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleBookHasNoAvailableCopyException(BookHasNoAvailableCopyException exception) {
        String errorMessage = MessageFormat.format("Book with ISBN {0} has no available copy.", exception.getISBN());
        return ErrorDto.builder().message(errorMessage).build();
    }


    @ExceptionHandler(BookAlreadyBorrowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDto handleBookAlreadyBorrowedException(BookAlreadyBorrowedException exception) {
        String errorMessage = MessageFormat.format("Book with ISBN {0} is already borrowed.", exception.getISBN());
        return ErrorDto.builder().message(errorMessage).build();
    }

    @ExceptionHandler(JwtTokenCreationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleJwtTokenCreationException(JwtTokenCreationException exception) {
        log.error("Unexpected error occurred during generating of JWT token.", exception);
        String errorMessage = "Could not login user. Try login later.";
        return ErrorDto.builder().message(errorMessage).build();
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleException(TokenExpiredException throwable) {
        log.error("Unknown error occurred.", throwable);
        return ErrorDto.builder().message("Unknown error occurred.").build();
    }
}
