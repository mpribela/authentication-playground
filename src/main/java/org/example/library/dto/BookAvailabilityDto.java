package org.example.library.dto;

import lombok.Builder;

@Builder
public record BookAvailabilityDto(int availableCopies) {
}
