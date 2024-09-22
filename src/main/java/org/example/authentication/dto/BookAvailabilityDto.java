package org.example.authentication.dto;

import lombok.Builder;

@Builder
public record BookAvailabilityDto(int availableCopies) {
}
