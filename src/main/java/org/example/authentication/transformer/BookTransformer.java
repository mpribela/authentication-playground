package org.example.authentication.transformer;

import org.example.authentication.data.BookDao;
import org.example.authentication.dto.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookTransformer {

    public BookDao toDao(BookDTO bookDTO) {
        return BookDao.builder()
                .title(bookDTO.title())
                .author(bookDTO.author())
                .ISBN(bookDTO.ISBN())
                .build();
    }

    public BookDTO toDTO(BookDao bookDao) {
        return BookDTO.builder()
                .author(bookDao.getAuthor())
                .ISBN(bookDao.getISBN())
                .title(bookDao.getTitle())
                .build();
    }
}
