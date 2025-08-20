package org.example.library.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BookListDto(List<BookDto> books) {
}
