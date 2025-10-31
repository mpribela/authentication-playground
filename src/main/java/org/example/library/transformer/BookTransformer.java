package org.example.library.transformer;

import org.example.library.data.BookEntity;
import org.example.library.dto.BookDto;
import org.example.library.dto.RegisterBookDto;
import org.example.library.service.filter.BookFilter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class BookTransformer {

    public BookEntity toEntity(RegisterBookDto registerBookDto) {
        return BookEntity.builder()
                .title(registerBookDto.title())
                .author(registerBookDto.author())
                .ISBN(registerBookDto.ISBN())
                .totalBorrows(0)
                .registered(Instant.now())
                .availableCopies(registerBookDto.copies())
                .build();
    }

    public BookEntity toEntity(BookFilter bookFilter) {
        return BookEntity.builder()
                .title(bookFilter.getTitle())
                .author(bookFilter.getAuthor())
                .ISBN(bookFilter.getISBN())
                .totalBorrows(0)
                .registered(Instant.now())
                .availableCopies(0)
                .build();
    }

    public BookDto toDTO(BookEntity bookEntity, boolean isBorrowedByUser) {
        return BookDto.builder()
                .author(bookEntity.getAuthor())
                .ISBN(bookEntity.getISBN())
                .title(bookEntity.getTitle())
                .availableCopies(bookEntity.getAvailableCopies())
                .isBorrowedByUser(isBorrowedByUser)
                .build();
    }
}
