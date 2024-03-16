package org.example.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.BookDto;
import org.example.authentication.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.example.authentication.security.JwtAuthenticationFilter.USER_ID_ATTRIBUTE;

@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/{bookId}/borrow")
    @ResponseStatus(HttpStatus.OK)
    public BookDto borrowBook(@PathVariable("bookId") String bookId,
                              @RequestAttribute(USER_ID_ATTRIBUTE) String userId) {
        return bookService.borrowBook(bookId, userId);
    }

    @PostMapping(value = "/{bookId}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable("bookId") String bookId,
                              @RequestAttribute(USER_ID_ATTRIBUTE) String userId) {
        bookService.returnBook(bookId, userId);
    }

    @GetMapping(value = "/{bookId}/exists")
    public void existsBook(@PathVariable("bookId") String bookId) {
        bookService.exists(bookId);
    }

    @PostMapping(value = "/register")
    public void registerBook(@RequestBody BookDto bookDTO) {
        bookService.registerBook(bookDTO);
    }
}
