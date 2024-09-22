package org.example.authentication.integration.authentication.open.v1;

import org.example.authentication.data.BookEntity;
import org.example.authentication.dto.BookAvailabilityDto;
import org.example.authentication.integration.base.AuthenticationBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.example.authentication.util.EndpointHelper.createHttpEntity;
import static org.example.authentication.util.EndpointHelper.createUrl;
import static org.example.authentication.util.builder.EntityBuilder.createBook;
import static org.springframework.http.HttpMethod.GET;

public class BookPublicEndpointAuthenticationTest extends AuthenticationBase {

    private static final String AVAILABLE_BOOK_URL = "/public/v1/book/{0}/available";

    @Nested
    class BookAvailability {

        @Test
        @DisplayName("when the user is authenticated then the user can look for a book")
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
        @DisplayName("when the user is anonymous then the user can look for a book")
        void test2() {
            //given
            BookEntity book = createBook().availableCopies(3).build();
            bookRepository.save(book);
            var expectedDto = BookAvailabilityDto.builder()
                    .availableCopies(3)
                    .build();
            var availableUrl = createUrl(AVAILABLE_BOOK_URL, book.getISBN());

            //when
            var response = testRestTemplate.exchange(availableUrl, GET, HttpEntity.EMPTY, BookAvailabilityDto.class);

            //then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(expectedDto);
        }

    }
}
