package org.example.authentication.controller;

import org.example.authentication.service.BookService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/v1")
public class PublicController {

    private final BookService bookService;

    public PublicController(BookService bookService) {
        this.bookService = bookService;
    }


}
