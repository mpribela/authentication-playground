package org.example.authentication.dto;

import lombok.*;

@Builder
public record RegisterBookDto(String title, String author, String ISBN, int copies) {
}
