package org.example.authentication.data;


import lombok.*;
import org.example.authentication.exception.book.BookAlreadyBorrowedException;
import org.example.authentication.exception.book.BookHasNoAvailableCopyException;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document("book")
public class BookEntity {
    @Id
    private String ISBN;
    private String title;
    private String author;
    private Instant registered;
    private int availableCopies;
    @Getter(AccessLevel.NONE)
    @Builder.Default
    private List<BorrowEntity> currentBorrows = new ArrayList<>();
    @Builder.Default
    private int totalBorrows = 0;

    public void borrow(String userId) {
        if (availableCopies == 0) {
            throw new BookHasNoAvailableCopyException(this.ISBN);
        }
        if (isBorrowedBy(userId)) {
            throw new BookAlreadyBorrowedException(this.ISBN, userId);
        }
        currentBorrows.add(new BorrowEntity(userId));
        totalBorrows++;
        availableCopies--;
    }

    public boolean returnBook(String userId) {
        BorrowEntity borrow = new BorrowEntity(userId);
        if (!isBorrowedBy(userId)) {
            return false;
        }
        currentBorrows.remove(borrow);
        availableCopies++;
        return true;
    }

    public boolean isBorrowedBy(String userId) {
        BorrowEntity borrow = new BorrowEntity(userId);
        if (currentBorrows == null) {
            currentBorrows = new ArrayList<>();
        }
        return currentBorrows.contains(borrow);
    }

    public void addCopies(int copies) {
        this.availableCopies+=copies;
    }
}
