package org.example.authentication.builder;

import org.example.authentication.data.BookEntity;
import org.example.authentication.data.UserEntity;
import org.example.authentication.data.UserRole;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class EntityBuilder {

    public static UserEntity.UserEntityBuilder createUser() {
        return UserEntity.builder()
                .username("username")
                .password("password")
                .created(Instant.parse("2024-07-15T16:23:32.00Z"))
                .userRoles(List.of(UserRole.ROLE_READER))
                .lastLogin(Instant.parse("2024-07-15T16:23:32.00Z"))
                .enabled(true);
    }

    public static BookEntity.BookEntityBuilder createBook() {
        return BookEntity.builder()
                .title("Harry Potter and the Philosopher's Stone")
                .author("J.K. Rowling")
                .ISBN("9781408855652")
                .totalBorrows(0)
                .registered(Instant.from(OffsetDateTime.of(2024, 7, 14, 16, 53, 12, 0, ZoneOffset.UTC)))
                .availableCopies(1);
    }
}
