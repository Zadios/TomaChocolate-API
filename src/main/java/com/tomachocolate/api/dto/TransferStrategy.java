package com.tomachocolate.api.dto;

import java.math.BigDecimal;

public record TransferStrategy(
        String fromParticipant,
        String toParticipant,
        BigDecimal amount
) {}