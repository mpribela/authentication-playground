package org.example.authentication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.data.BookDao;
import org.example.authentication.dto.BookDTO;
import org.example.authentication.exception.DataNotFoundException;
import org.example.authentication.repository.BookRepository;
import org.example.authentication.transformer.BookTransformer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;

    public BookService(BookRepository bookRepository, BookTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
    }

    //todo make it transactional
    public BookDTO borrowBook(String id) {
        BookDao book = bookRepository.findById(id).orElseThrow(DataNotFoundException::new);
        book.borrow();
        bookRepository.save(book);
        return bookTransformer.toDTO(book);
    }

    public void registerBook(BookDTO bookDTO) {
        BookDao book = bookTransformer.toDao(bookDTO);
        BookDao insertedBook = bookRepository.insert(book);
        log.info("Registered book {}.", insertedBook);
    }
}
