package org.example.authentication.dto;

import lombok.*;

@Builder
public record BookDTO(String title, String author, String ISBN) {
}
