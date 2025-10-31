package org.example.library.service.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.ExampleMatcher;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookFilter {
    public static final String TITLE_FIELD = "title";
    public static final String ISBN_FIELD = "ISBN";
    public static final String AUTHOR_FIELD = "author";
    public static final String CURRENT_BORROWS_FIELD = "currentBorrows";
    public static final String AVAILABLE_COPIES_FIELD = "availableCopies";
    public static final String TOTAL_BORROWS_FIELD = "totalBorrows";
    public static final String REGISTERED_FIELD = "registered";

    public static final ExampleMatcher BOOK_MATCHER = ExampleMatcher.matchingAll()
            .withIgnoreNullValues()
            .withMatcher(TITLE_FIELD, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(ISBN_FIELD, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(AUTHOR_FIELD, ExampleMatcher.GenericPropertyMatcher::contains)
            .withIgnoreCase()
            .withIgnorePaths(AVAILABLE_COPIES_FIELD, TOTAL_BORROWS_FIELD, REGISTERED_FIELD, CURRENT_BORROWS_FIELD);

    private String title;
    private String ISBN;
    private String author;
}
