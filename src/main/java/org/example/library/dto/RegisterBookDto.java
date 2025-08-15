package org.example.library.dto;

import lombok.*;

//todo validations
@Builder
public record RegisterBookDto(String title, String author, String ISBN, int copies) {
}
