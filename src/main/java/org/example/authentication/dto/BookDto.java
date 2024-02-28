package org.example.authentication.dto;

import lombok.*;

@Builder
public record BookDto(String title, String author, String ISBN) {
}
