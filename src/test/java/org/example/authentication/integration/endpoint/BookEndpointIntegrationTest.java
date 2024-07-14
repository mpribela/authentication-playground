package org.example.authentication.integration.endpoint;


import org.example.authentication.data.BookEntity;
import org.example.authentication.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BookEndpointIntegrationTest extends NoAuthenticationEndpointIntegrationTest {

    @Autowired
    BookRepository bookRepository;

    BookEntity harryPotterBook = BookEntity.builder()
            .title("Harry Potter and the Philosopher's Stone")
            .author("J.K. Rowling")
            .ISBN("9781408855652")
            .totalBorrows(0)
            .registered(Instant.from(OffsetDateTime.of(2024, 7, 14, 16, 53, 12, 0, ZoneOffset.UTC)))
            .availableCopies(1)
            .build();

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
            bookRepository.save(harryPotterBook);

            //when then
            mockMvc.perform(get("/book/" + harryPotterBook.getISBN() + "/exists"))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("when book does not exists then return 404")
        void test2() throws Exception {
            //when then
            mockMvc.perform(get("/book/" + harryPotterBook.getISBN() + "/exists"))
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
            //when then
            mockMvc.perform(post("/book/register")
                            .content("""
                                    {
                                        "title": "Harry Potter and the Philosopher's Stone",
                                        "author" : "J.K. Rowling",
                                        "ISBN": "9781408855652",
                                        "copies": 2
                                    }""")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("when the book is registered then update the available copies of the book")
        void test2() throws Exception {
            //given
            bookRepository.save(harryPotterBook);

            //when
            mockMvc.perform(post("/book/register")
                            .content("""
                                    {
                                        "title": "Harry Potter and the Philosopher's Stone",
                                        "author" : "J.K. Rowling",
                                        "ISBN": "9781408855652",
                                        "copies": 2
                                    }""")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            //then
            BookEntity bookEntity = bookRepository.findByISBN(harryPotterBook.getISBN()).get();
            assertThat(bookEntity.getAvailableCopies()).isEqualTo(3);
        }
    }
}
