package org.example.authentication.transformer;

import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookTransformer {

    public BookEntity toEntity(BookDto bookDto) {
        return BookEntity.builder()
                .title(bookDto.title())
                .author(bookDto.author())
                .ISBN(bookDto.ISBN())
                .build();
    }

    public BookDto toDTO(BookEntity bookEntity) {
        return BookDto.builder()
                .author(bookEntity.getAuthor())
                .ISBN(bookEntity.getISBN())
                .title(bookEntity.getTitle())
                .build();
    }
}
