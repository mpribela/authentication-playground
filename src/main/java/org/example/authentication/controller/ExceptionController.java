package org.example.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.ErrorDto;
import org.example.authentication.exception.DataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleDataNotFoundException(DataNotFoundException exception) {
        return ErrorDto.builder().message("Resource not found.").build();
    }

//    @ExceptionHandler(Exception.class)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorDto handleException(Exception throwable) {
//        log.error("Unknown error occurred.", throwable);
//        return ErrorDto.builder().message("Unknown error occurred.").build();
//    }
}
