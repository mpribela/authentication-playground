package org.example.authentication.transformer;

import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.example.authentication.dto.RegisterBookDto;
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
