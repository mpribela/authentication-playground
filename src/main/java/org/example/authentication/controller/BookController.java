package org.example.authentication.controller;

import org.example.authentication.dto.BookDTO;
import org.example.authentication.service.BookService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/borrow/{id}")
    public BookDTO borrowBook(@PathVariable("id") String id) {
        return bookService.borrowBook(id);
    }

    @PostMapping(value = "/register")
    public void registerBook(@RequestBody BookDTO bookDTO) {
        bookService.registerBook(bookDTO);
    }
}
