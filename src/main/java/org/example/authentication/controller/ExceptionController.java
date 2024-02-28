package org.example.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.ErrorDto;
import org.example.authentication.exception.BookNotFoundException;
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
    public ErrorDto handleDataNotFoundException(BookNotFoundException exception) {
        String errorMessage = MessageFormat.format("Book with id {0} not found.", exception.getId());
        return ErrorDto.builder().message(errorMessage).build();
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorDto handleException(Exception throwable) {
//        log.error("Unknown error occurred.", throwable);
//        return ErrorDto.builder().message("Unknown error occurred.").build();
//    }
}
