package org.example.library.controller.open.v1;

import org.example.library.dto.BookAvailabilityDto;
import org.example.library.dto.BookDto;
import org.example.library.dto.BookListDto;
import org.example.library.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.example.library.filter.JwtAuthenticationFilter.USER_ID_ATTRIBUTE;

@RestController
@RequestMapping("/public/v1/books")
@CrossOrigin(origins = "http://localhost:5173/")
public class BookPublicController {

    private final BookService bookService;

    public BookPublicController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/{ISBN}/available")
    @ResponseStatus(HttpStatus.OK)
    public BookAvailabilityDto isBookAvailable(@PathVariable("ISBN") String ISBN) {
        return bookService.isAvailable(ISBN);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public BookListDto getAllBooks() {
        return bookService.getAllBooks(null);
    }

    @GetMapping(value = "/{ISBN}")
    @ResponseStatus(HttpStatus.OK)
    public BookDto getBook(@PathVariable("ISBN") String ISBN) {
        return bookService.getBook(ISBN);
    }
}
