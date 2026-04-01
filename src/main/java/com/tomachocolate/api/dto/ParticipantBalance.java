package com.tomachocolate.api.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ParticipantBalance(
        String name,
        BigDecimal totalPaid,
        BigDecimal balance
) {}
