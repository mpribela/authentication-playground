package org.example.authentication.builder;

import org.example.authentication.data.UserEntity;
import org.example.authentication.data.UserRole;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class EntityBuilder {

    public static UserEntity.UserEntityBuilder createUser() {
        return UserEntity.builder()
                .id("id")
                .username("username")
                .created(OffsetDateTime.MIN)
                .userRoles(List.of(UserRole.ROLE_ADMIN))
                .lastLogin(OffsetDateTime.of(2024,3,20,17,0,0,0, ZoneOffset.UTC))
                .enabled(true);
    }
}
