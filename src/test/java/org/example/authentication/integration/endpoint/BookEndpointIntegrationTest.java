package org.example.authentication.integration.endpoint;


import org.example.authentication.data.BookEntity;
import org.example.authentication.data.BorrowEntity;
import org.example.authentication.integration.base.NoAuthenticationBase;
import org.example.authentication.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.example.authentication.builder.EntityBuilder.createBook;
import static org.example.authentication.filter.JwtAuthenticationFilter.USER_ID_ATTRIBUTE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookEndpointIntegrationTest extends NoAuthenticationBase {

    @Autowired
    BookRepository bookRepository;

    @AfterEach
    void tearDown() {
        bookRepository.deleteAll();
    }

    @Nested
    class ExistsBook {

        @Test
        @DisplayName("when book exists then return 204")
        void test() throws Exception {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(get("/book/" + book.getISBN() + "/exists"))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("when book does not exists then return 404")
        void test2() throws Exception {
            //when then
            mockMvc.perform(get("/book/9781408855652/exists"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{'message':'Book with ISBN 9781408855652 not found.'}"));
        }
    }

    @Nested
    class RegisterBook {

        @Test
        @DisplayName("when the book is not registered then register the book")
        void test() throws Exception {
            //given
            BookEntity book = createBook().build();

            //when then
            mockMvc.perform(post("/book/register")
                            .content(objectMapper.writeValueAsString(book))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("when the book is registered then update the available copies of the book")
        void test2() throws Exception {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);

            //when
            mockMvc.perform(post("/book/register")
                            .content(objectMapper.writeValueAsString(book))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            //then
            BookEntity bookInDatabase = bookRepository.findByISBN(book.getISBN()).get();
            assertThat(bookInDatabase.getAvailableCopies()).isEqualTo(3);
        }
    }

    @Nested
    class BorrowBook {

        @Test
        @DisplayName("when book exists and there is available copy and book is not borrowed already by the user " +
                "then the book is borrowed")
        void test() throws Exception {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(post("/book/" + book.getISBN() + "/borrow")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpectAll(
                            jsonPath("$.title").value("Harry Potter and the Philosopher's Stone"),
                            jsonPath("$.author").value("J.K. Rowling"),
                            jsonPath("$.ISBN").value("9781408855652"),
                            jsonPath("$.availableCopies").value(0));

            BookEntity bookInDatabase = bookRepository.findByISBN(book.getISBN()).get();
            assertThat(bookInDatabase.isBorrowedBy("1")).isTrue();
            assertThat(bookInDatabase.getTotalBorrows()).isEqualTo(1);
        }

        @Test
        @DisplayName("when book does not exist then return 404")
        void test2() throws Exception {
            //when then
            mockMvc.perform(post("/book/9781408855652/borrow")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{'message':'Book with ISBN 9781408855652 not found.'}"));
        }

        @Test
        @DisplayName("when book has no available copies then return 404")
        void test3() throws Exception {
            //given
            BookEntity book = createBook()
                    .availableCopies(0)
                    .build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(post("/book/" + book.getISBN() + "/borrow")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{'message':'Book with ISBN 9781408855652 has no available copy.'}"));
        }

        @Test
        @DisplayName("when book is already borrowed by the user then return 409")
        void test4() throws Exception {
            //given
            BookEntity book = createBook()
                    .currentBorrows(List.of(new BorrowEntity("1")))
                    .build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(post("/book/" + book.getISBN() + "/borrow")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isConflict())
                    .andExpect(content().json("{'message':'Book with ISBN 9781408855652 is already borrowed.'}"));
        }
    }

    @Nested
    class ReturnBook {
        @Test
        @DisplayName("when book exists and is borrowed by the user then return book")
        void test() throws Exception {
            //given
            BookEntity book = createBook()
                    .currentBorrows(List.of(new BorrowEntity("1")))
                    .availableCopies(3)
                    .build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(post("/book/" + book.getISBN() + "/return")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            BookEntity bookInDatabase = bookRepository.findByISBN(book.getISBN()).get();
            assertThat(bookInDatabase.getAvailableCopies()).isEqualTo(4);
            assertThat(bookInDatabase.isBorrowedBy("1")).isFalse();
        }

        @Test
        @DisplayName("when book does not exist then return 404")
        void test2() throws Exception {
            //when then
            mockMvc.perform(post("/book/9781408855652/return")
                            .requestAttr(USER_ID_ATTRIBUTE, "1"))
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("{'message':'Book with ISBN 9781408855652 not found.'}"));
        }

        @Test
        @DisplayName("when book exists but is not borrowed by the user then book is not returned and 204 is returned")
        void test3() throws Exception {
            //given
            BookEntity book = createBook()
                    .currentBorrows(List.of(new BorrowEntity("1")))
                    .availableCopies(3)
                    .build();
            bookRepository.save(book);

            //when then
            mockMvc.perform(post("/book/" + book.getISBN() + "/return")
                            .requestAttr(USER_ID_ATTRIBUTE, "2"))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            BookEntity bookInDatabase = bookRepository.findByISBN(book.getISBN()).get();
            assertThat(bookInDatabase.getAvailableCopies()).isEqualTo(3);
            assertThat(bookInDatabase.isBorrowedBy("1")).isTrue();
        }
    }
}
