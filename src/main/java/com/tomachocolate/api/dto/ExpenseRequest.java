package com.tomachocolate.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record ExpenseRequest(
        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "El monto no puede estar vacío")
        @Positive(message = "El monto debe ser mayor a cero")
        BigDecimal amount,

        @NotNull(message = "Debes indicar quién pagó")
        Long payerId,

        @NotNull(message = "El ID de la juntada es obligatorio")
        UUID meetingId
) {}