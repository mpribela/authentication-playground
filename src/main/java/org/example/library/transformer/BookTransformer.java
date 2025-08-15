package org.example.library.transformer;

import org.example.library.data.BookEntity;
import org.example.library.dto.BookDto;
import org.example.library.dto.RegisterBookDto;
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

    public BookDto toDTO(BookEntity bookEntity) {
        return BookDto.builder()
                .author(bookEntity.getAuthor())
                .ISBN(bookEntity.getISBN())
                .title(bookEntity.getTitle())
                .availableCopies(bookEntity.getAvailableCopies())
                .build();
    }
}
