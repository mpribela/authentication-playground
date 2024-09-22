package org.example.authentication.integration.authentication;


import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookAvailabilityDto;
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
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public class BookEndpointAuthenticationTest extends AuthenticationBase {

    private static final String REGISTER_BOOK_URL = "/book/register";
    private static final String BORROW_BOOK_URL = "/book/{0}/borrow";
    private static final String RETURN_BOOK_URL = "/book/{0}/return";
    private static final String AVAILABLE_BOOK_URL = "/book/{0}/available";

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
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(bookRepository.findByISBN(book.ISBN())).isPresent();
        }

        @Test
        @DisplayName("when the user has NO role ADMIN then the action is forbidden")
        void test2() {
            //given
            String jwt = login(reader);
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity(jwt, book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(bookRepository.findByISBN(book.ISBN())).isNotPresent();
        }

        @Test
        @DisplayName("when the token is expired then the action is forbidden")
        void test3() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity(EXPIRED_TOKEN, book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(bookRepository.findByISBN(book.ISBN())).isNotPresent();
        }

        @Test
        @DisplayName("when no authentication token si provided then the action is forbidden")
        void test4() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = new HttpEntity<>(book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(bookRepository.findByISBN(book.ISBN())).isNotPresent();
        }

        @Test
        @DisplayName("when invalid authentication token si provided then the action is forbidden")
        void test5() {
            //given
            BookDto book = bookTransformer.toDTO(createBook().build());
            var httpEntity = createHttpEntity("", book);

            //when
            var response = testRestTemplate.exchange(REGISTER_BOOK_URL, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(bookRepository.findByISBN(book.ISBN())).isNotPresent();
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
            var response = testRestTemplate.exchange(borrowUrl, POST, httpEntity, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(expectedBookDto);
        }

        @Test
        @DisplayName("when the user has NO role READER then the action is forbidden")
        void test2() {
            //given
            String token = login(userWithoutRoles);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var borrowUrl = createUrl(BORROW_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(borrowUrl, POST, httpEntity, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when the user is not authenticated then the action is forbidden")
        void test3() {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var borrowUrl = createUrl(BORROW_BOOK_URL, book.getISBN());

            //when
            var response = testRestTemplate.exchange(borrowUrl, POST, HttpEntity.EMPTY, BookDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    class BookAvailability {

        @Test
        @DisplayName("when the user has role READER then the user can look for a book")
        void test() {
            //given
            String token = login(reader);
            BookEntity book = createBook().availableCopies(3).build();
            bookRepository.save(book);
            var expectedDto = BookAvailabilityDto.builder()
                    .availableCopies(3)
                    .build();
            var availableUrl = createUrl(AVAILABLE_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(availableUrl, GET, httpEntity, BookAvailabilityDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(expectedDto);
        }

        @Test
        @DisplayName("when the user has NO role READER then the action is forbidden")
        void test2() {
            //given
            String token = login(userWithoutRoles);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var availableUrl = createUrl(AVAILABLE_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(availableUrl, GET, httpEntity, BookAvailabilityDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        @DisplayName("when the user is NOT authenticated then the action is forbidden")
        void test3() {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var availableUrl = createUrl(AVAILABLE_BOOK_URL, book.getISBN());

            //when
            var response = testRestTemplate.exchange(availableUrl, GET, HttpEntity.EMPTY, BookAvailabilityDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    @Nested
    class BookReturn {

        @Test
        @DisplayName("when the user has role READER then the user can return a book")
        void test() {
            //given
            String token = login(reader);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var returnUrl = createUrl(RETURN_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(returnUrl, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        @DisplayName("when the user has NO role READER then the action is forbidden")
        void test2() {
            //given
            String token = login(userWithoutRoles);
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var returnUrl = createUrl(RETURN_BOOK_URL, book.getISBN());
            var httpEntity = createHttpEntity(token, null);

            //when
            var response = testRestTemplate.exchange(returnUrl, POST, httpEntity, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        }

        @Test
        @DisplayName("when the user is NOT authenticated then the action is forbidden")
        void test3() {
            //given
            BookEntity book = createBook().build();
            bookRepository.save(book);
            var returnUrl = createUrl(RETURN_BOOK_URL, book.getISBN());

            //when
            var response = testRestTemplate.exchange(returnUrl, POST, HttpEntity.EMPTY, Void.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }
    }

    private HttpEntity<BookDto> createHttpEntity(String token, BookDto book) {
        var headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(book, headers);
    }

    private String createUrl(String url, String isbn) {
        return MessageFormat.format(url, isbn);
    }
}
