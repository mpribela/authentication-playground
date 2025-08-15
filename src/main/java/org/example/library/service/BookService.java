package org.example.library.service;

import lombok.extern.slf4j.Slf4j;
import org.example.library.data.BookEntity;
import org.example.library.dto.BookAvailabilityDto;
import org.example.library.dto.BookDto;
import org.example.library.dto.RegisterBookDto;
import org.example.library.exception.book.BookNotFoundException;
import org.example.library.repository.BookRepository;
import org.example.library.transformer.BookTransformer;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class BookService {

    private final BookRepository bookRepository;
    private final BookTransformer bookTransformer;

    public BookService(BookRepository bookRepository, BookTransformer bookTransformer) {
        this.bookRepository = bookRepository;
        this.bookTransformer = bookTransformer;
    }

    public BookAvailabilityDto isAvailable(String ISBN) {
        BookEntity book = bookRepository.findByISBN(ISBN)
                .orElseThrow(() -> new BookNotFoundException(ISBN));
        return BookAvailabilityDto.builder()
                .availableCopies(book.getAvailableCopies())
                .build();
    }

    //todo make it transactional
    public BookDto borrowBook(String ISBN, String userId) {
        BookEntity book = bookRepository.findByISBN(ISBN)
                .orElseThrow(() -> new BookNotFoundException(ISBN));
        book.borrow(userId);
        bookRepository.save(book);
        return bookTransformer.toDTO(book);
    }

    //todo make it transactional
    public void returnBook(String ISBN, String userId) {
        BookEntity book = bookRepository.findByISBN(ISBN).orElseThrow(() -> new BookNotFoundException(ISBN));
        boolean returned = book.returnBook(userId);
        if (returned) {
            bookRepository.save(book);
            log.info("Book with ISBN {} successfully returned by the user {}.", ISBN, userId);
        } else {
            log.info("Book with ISBN {} is not borrowed by the user {} and therefore is not returned.", ISBN, userId);
        }
    }

    //todo make it transactional
    public void registerBook(RegisterBookDto registerBookDTO) {
        Optional<BookEntity> bookFromDatabase = bookRepository.findByISBN(registerBookDTO.ISBN());
        if (bookFromDatabase.isPresent()) {
            addCopiesOfBook(registerBookDTO, bookFromDatabase.get());
        } else {
            registerNewBook(registerBookDTO);
        }
    }

    private void addCopiesOfBook(RegisterBookDto registerBookDTO, BookEntity book) {
        book.addCopies(registerBookDTO.copies());
        bookRepository.save(book);
        log.info("Added {} copies to book with ISBN {}.", registerBookDTO.copies(), registerBookDTO.ISBN());
    }

    private void registerNewBook(RegisterBookDto registerBookDTO) {
        BookEntity book = bookTransformer.toEntity(registerBookDTO);
        BookEntity insertedBook = bookRepository.insert(book);
        log.info("Registered book {}.", insertedBook);
    }

    /*
    todo:
      - search by Title
      - search by Author
     */
}
