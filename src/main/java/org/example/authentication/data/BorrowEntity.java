package org.example.authentication.data;

import lombok.*;

import java.time.Instant;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class BorrowEntity {
    private String user;
    @EqualsAndHashCode.Exclude
    private Instant borrowTime;

    public BorrowEntity(String user) {
        this.user = user;
        this.borrowTime = Instant.now();
    }
}
