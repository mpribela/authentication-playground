package org.example.library.dto;

import lombok.Builder;

@Builder
public record BookDto(String title, String author, String ISBN, int availableCopies) {
}
