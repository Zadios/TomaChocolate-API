package com.tomachocolate.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MeetingRequest(
        @NotBlank(message = "Ingrese el nombre")
        String name,

        @NotNull(message = "Ingrese el número de participantes")
        @Min(value = 2, message = "Mínimo 2 participantes")
        Integer participantCount
) {}
