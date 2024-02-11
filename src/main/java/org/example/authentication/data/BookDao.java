package org.example.authentication.data;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document("book")
public class BookDao {
    @Id
    private String id;
    private String title;
    private String author;
    private OffsetDateTime registered;
    private int borrows;
    private String ISBN;

    public void borrow() {
        borrows++;
    }
}
