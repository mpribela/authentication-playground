package org.example.authentication.dto;

import lombok.*;

//todo validations
@Builder
public record RegisterBookDto(String title, String author, String ISBN, int copies) {
}
