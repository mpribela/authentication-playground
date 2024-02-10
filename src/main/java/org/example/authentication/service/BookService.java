package org.example.authentication.service;

import org.example.authentication.dto.BookDTO;
import org.springframework.stereotype.Component;

@Component
public class BookService {

    public BookDTO getBook(String id) {
        return new BookDTO();
    }

    public void registerBook(BookDTO bookDTO) {
        
    }
}
