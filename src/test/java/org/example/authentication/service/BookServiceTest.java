package org.example.authentication.service;

import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.example.authentication.exception.BookAlreadyBorrowedException;
import org.example.authentication.exception.BookNotFoundException;
import org.example.authentication.repository.BookRepository;
import org.example.authentication.transformer.BookTransformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.example.authentication.builder.EntityBuilder.createBook;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    BookTransformer bookTransformer;
    @InjectMocks
    BookService bookService;

    String bookId = "123";
    String userId = "456";

    @Test
    @DisplayName("when book exists then do not throw exception")
    void existsTest1() {
        //given
        given(bookRepository.existsById(bookId)).willReturn(true);

        //when then
        assertDoesNotThrow(() -> bookService.exists(bookId));
    }

    @Test
    @DisplayName("when book does not exist then throw exception")
    void existsTest2() {
        //given
        given(bookRepository.existsById(bookId)).willReturn(false);

        //when then
        assertThrows(BookNotFoundException.class, () -> bookService.exists(bookId));
    }

    @Test
    @DisplayName("when book exists and is not borrowed then set borrowed and increment count")
    void borrowBookTest1() {
        //given
        var argumentCaptor = ArgumentCaptor.forClass(BookEntity.class);
        BookDto expectedBookDto = mock(BookDto.class);
        BookEntity book = createBook()
                .id(bookId)
                .ISBN("book-number")
                .author("author")
                .title("title")
                .borrowedBy(null)
                .borrows(3).build();
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));
        given(bookTransformer.toDTO(book)).willReturn(expectedBookDto);

        //when
        BookDto bookDto = bookService.borrowBook(bookId, userId);

        //then
        verify(bookRepository).save(argumentCaptor.capture());
        BookEntity updatedBook = argumentCaptor.getValue();
        assertEquals(userId, updatedBook.getBorrowedBy());
        assertEquals(4, updatedBook.getBorrows());
        assertEquals(expectedBookDto, bookDto);
    }

    @Test
    @DisplayName("when book exists but is borrowed then throw exception")
    void borrowBookTest2() {
        //given
        BookEntity book = createBook().id(bookId).borrowedBy("789").build();
        given(bookRepository.findById(bookId)).willReturn(Optional.of(book));

        //when then
        assertThrows(BookAlreadyBorrowedException.class, () -> bookService.borrowBook(bookId, userId));
    }

    @Test
    @DisplayName("when book does not exist then throw exception")
    void borrowBookTest3() {
        //given
        given(bookRepository.findById(bookId)).willReturn(Optional.empty());

        //when then
        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(bookId, userId));
    }

    //todo m.pribela return book

    //todo m.pribela register book
}