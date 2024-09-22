package org.example.authentication.controller.open.v1;

import org.example.authentication.dto.BookAvailabilityDto;
import org.example.authentication.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/v1/book")
public class BookPublicController {

    private final BookService bookService;

    public BookPublicController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/{ISBN}/available")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BookAvailabilityDto isBookAvailable(@PathVariable("ISBN") String ISBN) {
        return bookService.isAvailable(ISBN);
    }
}
