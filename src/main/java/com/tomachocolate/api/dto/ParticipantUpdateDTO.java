package com.tomachocolate.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ParticipantUpdateDTO(
        @NotBlank String name
) {}
