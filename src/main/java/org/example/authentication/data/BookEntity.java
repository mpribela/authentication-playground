package org.example.authentication.data;


import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.example.authentication.exception.BookAlreadyBorrowedException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document("book")
public class BookEntity {
    @Id
    private String id;
    private String title;
    private String author;
    private OffsetDateTime registered;
    private int borrows;
    private String ISBN;
    private String borrowedBy;

    public void borrow(String userId) {
        if (StringUtils.isNotBlank(this.borrowedBy)) {
            throw new BookAlreadyBorrowedException(this.id);
        }
        borrowedBy = userId;
        borrows++;
    }

    public boolean returnBook() {
        if (StringUtils.isBlank(borrowedBy)) {
            return false;
        }
        borrowedBy = null;
        return true;
    }
}
