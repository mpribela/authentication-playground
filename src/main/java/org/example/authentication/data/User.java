package org.example.authentication.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Builder
@Getter
@ToString(exclude = "password")
@EqualsAndHashCode
@Document("user")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private UserRole userRole;
    private OffsetDateTime created;
    private OffsetDateTime lastLogin;
}
