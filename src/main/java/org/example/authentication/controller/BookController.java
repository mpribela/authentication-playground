package org.example.authentication.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.dto.BookDto;
import org.example.authentication.dto.RegisterBookDto;
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

    @PostMapping(value = "/{ISBN}/borrow")
    @ResponseStatus(HttpStatus.OK)
    public BookDto borrowBook(@PathVariable("ISBN") String ISBN,
                              @RequestAttribute(USER_ID_ATTRIBUTE) String userId) {
        return bookService.borrowBook(ISBN, userId);
    }

    @PostMapping(value = "/{ISBN}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnBook(@PathVariable("ISBN") String ISBN,
                              @RequestAttribute(USER_ID_ATTRIBUTE) String userId) {
        bookService.returnBook(ISBN, userId);
    }

    @GetMapping(value = "/{ISBN}/exists")
    public void existsBook(@PathVariable("ISBN") String ISBN) {
        bookService.exists(ISBN);
    }

    @PostMapping(value = "/register")
    public void registerBook(@RequestBody RegisterBookDto registerBookDTO) {
        bookService.registerBook(registerBookDTO);
    }
}
