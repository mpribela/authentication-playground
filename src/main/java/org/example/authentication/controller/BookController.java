package org.example.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.BookDto;
import org.example.authentication.service.BookService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/{id}/borrow")
    public BookDto borrowBook(@PathVariable("id") String id) {
        return bookService.borrowBook(id);
    }

    @GetMapping(value = "/{id}/exists")
    public void existsBook(@PathVariable("id") String id) {
        bookService.exists(id);
    }

    @PostMapping(value = "/register")
    public void registerBook(@RequestBody BookDto bookDTO) {
        bookService.registerBook(bookDTO);
    }
}
