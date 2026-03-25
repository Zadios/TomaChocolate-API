package com.tomachocolate.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MeetingRequest(
        @NotBlank(message = "El nombre de la juntada no puede estar vacío")
        String name,

        @NotNull(message = "Debes indicar cuántas personas son")
        @Min(value = 2, message = "Mínimo tienen que ser 2 personas")
        Integer participantCount
) {}
