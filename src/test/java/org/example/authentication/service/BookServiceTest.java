package org.example.authentication.service;

import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.example.authentication.dto.RegisterBookDto;
import org.example.authentication.exception.book.BookNotFoundException;
import org.example.authentication.repository.BookRepository;
import org.example.authentication.transformer.BookTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    BookTransformer bookTransformer;
    @InjectMocks
    BookService bookService;

    String bookISBN = "123";
    String userId = "456";

    @Test
    @DisplayName("when book exists then do not throw exception")
    void existsTest1() {
        //given
        given(bookRepository.existsByISBN(bookISBN)).willReturn(true);

        //when then
        assertDoesNotThrow(() -> bookService.exists(bookISBN));
    }

    @Test
    @DisplayName("when book does not exist then throw exception")
    void existsTest2() {
        //given
        given(bookRepository.existsByISBN(bookISBN)).willReturn(false);

        //when then
        assertThrows(BookNotFoundException.class, () -> bookService.exists(bookISBN));
    }

    @Test
    @DisplayName("when book exists then book is borrowed")
    void borrowBookTest1() {
        //given
        BookEntity bookMock = mock(BookEntity.class);
        BookDto bookDtoMock = mock(BookDto.class);
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.of(bookMock));
        given(bookTransformer.toDTO(bookMock)).willReturn(bookDtoMock);

        //when
        BookDto bookDto = bookService.borrowBook(bookISBN, userId);

        //then
        verify(bookMock).borrow(userId);
        verify(bookRepository).save(bookMock);
        verify(bookTransformer).toDTO(bookMock);
        assertSame(bookDtoMock, bookDto);
    }

    @Test
    @DisplayName("when book does not exist then throw exception")
    void borrowBookTest2() {
        //given
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.empty());

        //when then
        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(bookISBN, userId));
    }

    @Test
    @DisplayName("when book exists and is returned then database is updated")
    void returnBookTest1() {
        //given
        BookEntity bookMock = mock(BookEntity.class);
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.of(bookMock));
        given(bookMock.returnBook(userId)).willReturn(true);

        //when
        bookService.returnBook(bookISBN, userId);

        //then
        verify(bookMock).returnBook(userId);
        verify(bookRepository).save(bookMock);
    }

    @Test
    @DisplayName("when book exists and is not returned then database is not updated")
    void returnBookTest2() {
        //given
        BookEntity bookMock = mock(BookEntity.class);
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.of(bookMock));
        given(bookMock.returnBook(userId)).willReturn(false);

        //when
        bookService.returnBook(bookISBN, userId);

        //then
        verify(bookMock).returnBook(userId);
        verify(bookRepository, never()).save(any());
    }

    @Test
    @DisplayName("when book does not exist then exception is thrown")
    void returnBookTest3() {
        //given
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.empty());

        //when then
        assertThrows(BookNotFoundException.class, () -> bookService.returnBook(bookISBN, userId));
    }

    @Test
    @DisplayName("when book is already registered then copies are added and saved to database")
    void registerBookTest1() {
        //given
        RegisterBookDto registerBookDto = RegisterBookDto.builder()
                .ISBN(bookISBN)
                .copies(3)
                .build();
        BookEntity bookMock = mock(BookEntity.class);
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.of(bookMock));

        //when
        bookService.registerBook(registerBookDto);

        //then
        verify(bookMock).addCopies(3);
        verify(bookRepository).save(bookMock);
        verify(bookRepository, never()).insert(any(BookEntity.class));
    }

    @Test
    @DisplayName("when book is not registered then insert the book to database")
    void registerBookTest2() {
        //given
        RegisterBookDto registerBookDto = RegisterBookDto.builder()
                .ISBN(bookISBN)
                .build();
        BookEntity expectedBookToSave = mock(BookEntity.class);
        given(bookRepository.findByISBN(bookISBN)).willReturn(Optional.empty());
        given(bookTransformer.toEntity(registerBookDto)).willReturn(expectedBookToSave);

        //when
        bookService.registerBook(registerBookDto);

        //then
        verify(bookRepository, never()).save(any());
        verify(bookRepository).insert(expectedBookToSave);
    }
}