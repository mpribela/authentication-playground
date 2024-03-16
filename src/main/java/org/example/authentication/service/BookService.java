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
    public BookDto borrowBook(String bookId, String userId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        book.borrow(userId);
        bookRepository.save(book);
        return bookTransformer.toDTO(book);
    }

    //todo make it transactional
    public void returnBook(String bookId, String userId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        boolean returned = book.returnBook();
        bookRepository.save(book);
        if (returned) {
            log.info("Book with ID {} successfully returned by user {}.", bookId, userId);
        } else {
            log.info("Book with ID {} is already returned.", bookId);
        }
    }

    public void registerBook(BookDto bookDTO) {
        BookEntity book = bookTransformer.toEntity(bookDTO);
        BookEntity insertedBook = bookRepository.insert(book);
        log.info("Registered book {}.", insertedBook);
    }
}
