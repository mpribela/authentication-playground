package org.example.authentication.data;

import org.example.authentication.exception.book.BookAlreadyBorrowedException;
import org.example.authentication.exception.book.BookHasNoAvailableCopyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookEntityTest {

    String userId = "123";
    String userId2 = "456";

    @Test
    @DisplayName("when copies are available and book is not borrowed by the user then add user as borrower and adjust total borrows and available copies")
    void borrowTest1() {
        //given
        var borrowers = new ArrayList<BorrowEntity>();
        borrowers.add(new BorrowEntity(userId2));
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .totalBorrows(5)
                .currentBorrows(borrowers)
                .build();

        //when
        book.borrow(userId);

        //then
        assertEquals(0, book.getAvailableCopies());
        assertEquals(6, book.getTotalBorrows());
        assertTrue(book.isBorrowedBy(userId), "User should be registered as a borrower.");
        assertTrue(book.isBorrowedBy(userId2), "Already borrowed entries should not be affected.");
    }

    @Test
    @DisplayName("when no copies are available then throw exception")
    void borrowTest2() {
        //given
        BookEntity book = BookEntity.builder()
                .availableCopies(0)
                .currentBorrows(List.of())
                .build();

        //when
        assertThrows(BookHasNoAvailableCopyException.class, () -> book.borrow(userId));
    }

    @Test
    @DisplayName("when book is already borrowed by the user then throw exception")
    void borrowTest3() {
        //given
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .currentBorrows(List.of(new BorrowEntity(userId)))
                .build();

        //when
        assertThrows(BookAlreadyBorrowedException.class, () -> book.borrow(userId));
    }

    @Test
    @DisplayName("when current borrows are null then it will not throw an exception")
    void borrowTest4() {
        //given
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .currentBorrows(null)
                .build();

        //when then
        assertDoesNotThrow(() -> book.borrow(userId));
    }

    @Test
    @DisplayName("when book is borrowed by the user then user is returned from the current borrowers and available copies are adjusted")
    void returnBookTest1() {
        //given
        var borrowers = new ArrayList<BorrowEntity>();
        borrowers.add(new BorrowEntity(userId));
        borrowers.add(new BorrowEntity(userId2));
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .currentBorrows(borrowers)
                .build();

        //when
        boolean returned = book.returnBook(userId);

        //then
        assertTrue(returned, "Book should be returned successfully.");
        assertEquals(2, book.getAvailableCopies());
        assertFalse(book.isBorrowedBy(userId), "Book should not be borrowed by the user anymore.");
        assertTrue(book.isBorrowedBy(userId2), "Others users should not be affected.");
    }

    @Test
    @DisplayName("when book is not borrowed by the user then book is not adjusted and false is returned")
    void returnBookTest2() {
        //given
        var borrowers = new ArrayList<BorrowEntity>();
        borrowers.add(new BorrowEntity(userId2));
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .currentBorrows(borrowers)
                .build();

        //when
        boolean returned = book.returnBook(userId);

        //then
        assertFalse(returned, "Book should NOT be returned.");
        assertEquals(1, book.getAvailableCopies());
        assertFalse(book.isBorrowedBy(userId), "Book should not be borrowed by the user.");
        assertTrue(book.isBorrowedBy(userId2), "Others users should not be affected.");
    }

    @Test
    @DisplayName("when user is in the current borrowers then return true")
    void isBorrowedByTest1() {
        //given
        var borrowers = new ArrayList<BorrowEntity>();
        borrowers.add(new BorrowEntity(userId2));
        borrowers.add(new BorrowEntity(userId));
        BookEntity book = BookEntity.builder()
                .currentBorrows(borrowers)
                .build();

        //when
        boolean result = book.isBorrowedBy(userId);

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("when user is NOT in the current borrowers then return false")
    void isBorrowedByTest2() {
        //given
        var borrowers = new ArrayList<BorrowEntity>();
        borrowers.add(new BorrowEntity(userId2));
        BookEntity book = BookEntity.builder()
                .currentBorrows(borrowers)
                .build();

        //when
        boolean result = book.isBorrowedBy(userId);

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("when the current borrowers object is null then return false")
    void isBorrowedByTest3() {
        //given
        BookEntity book = BookEntity.builder()
                .currentBorrows(null)
                .build();

        //when
        boolean result = book.isBorrowedBy(userId);

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("new copies are added to already available copies")
    void addCopiesTest1() {
        //given
        BookEntity book = BookEntity.builder()
                .availableCopies(1)
                .build();

        //when
        book.addCopies(2);

        //then
        assertEquals(3, book.getAvailableCopies());
    }
}