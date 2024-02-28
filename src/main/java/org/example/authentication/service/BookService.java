package org.example.authentication.service;

import lombok.extern.slf4j.Slf4j;
import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.example.authentication.exception.BookNotFoundException;
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

    public void exists(String id) {
        boolean exists = bookRepository.existsById(id);
        if (!exists) {
            throw new BookNotFoundException(id);
        }
    }

    //todo make it transactional
    public BookDto borrowBook(String id) {
        BookEntity book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        book.borrow();
        bookRepository.save(book);
        return bookTransformer.toDTO(book);
    }

    public void registerBook(BookDto bookDTO) {
        BookEntity book = bookTransformer.toEntity(bookDTO);
        BookEntity insertedBook = bookRepository.insert(book);
        log.info("Registered book {}.", insertedBook);
    }
}
