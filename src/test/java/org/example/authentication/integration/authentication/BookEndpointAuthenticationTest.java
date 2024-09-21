package org.example.authentication.integration.authentication;


import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookDto;
import org.example.authentication.integration.base.AuthenticationBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.text.MessageFormat;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.example.authentication.builder.EntityBuilder.createBook;

public class BookEndpointAuthenticationTest extends AuthenticationBase {

    private static final String REGISTER_BOOK_URL = "/book/register";
    private static final String BORROW_BOOK_URL = "/book/{0}/borrow";
    private static final String RETURN_BOOK_URL = "/book/{0}/return";
    private static final String EXISTS_BOOK_URL = "/book/{0}/exists";

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @Nested
    class BookRegistration {

        @Test
        @DisplayName("when the user has role ADMIN then the user can register a book")
        void test() {
            //given
            String token = login(admin);
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity(token, book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, HttpMethod.POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("when the user has NO role ADMIN then the user cannot register a book and forbidden is returned")
        void test2() {
            //given
            String jwt = login(reader);
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity(jwt, book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, HttpMethod.POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when the token is expired then cannot register a book and return forbidden")
        void test3() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity(EXPIRED_TOKEN, book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, HttpMethod.POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when no authentication token si provided then cannot register a book and return forbidden")
        void test4() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = new HttpEntity<>(book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, HttpMethod.POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when invalid authentication token si provided then cannot register a book and return forbidden")
        void test5() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity("", book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, HttpMethod.POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    class BookBorrow {

        @Test
        @DisplayName("when the user has role READER then the user can borrow a book")
        void test() {
            //given
            String token = login(reader);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var expectedBookDto = BookDto.builder()
                    .ISBN(book.getISBN())
                    .author(book.getAuthor())
                    .title(book.getTitle())
                    .availableCopies(0)
                    .build();
            var borrowUrl = createUrl(BORROW_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(borrowUrl, HttpMethod.POST, httpEntity, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(expectedBookDto);
        }

        @Test
        @DisplayName("when the user has NO role READER then the user cannot borrow a book")
        void test2() {
            //given
            String token = login(admin);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var borrowUrl = createUrl(BORROW_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(borrowUrl, HttpMethod.POST, httpEntity, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when the user is not authenticated then cannot borrow a book")
        void test3() {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var borrowUrl = createUrl(BORROW_BOOK_URL, book.getISBN());

            //when
            var response = testRestTemplate.exchange(borrowUrl, HttpMethod.POST, HttpEntity.EMPTY, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    //todo add other book endpoint tests

    private HttpEntity<BookDto> createHttpEntity(String jwt, BookDto book) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        return new HttpEntity<>(book, headers);
    }

    private String createUrl(String url, String isbn) {
        return MessageFormat.format(url, isbn);
    }
}
