package com.tomachocolate.api.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record MeetingBalanceResponse(
        BigDecimal totalAmount,
        BigDecimal averagePerPerson,
        List<ParticipantBalance> participantBalances,
        List<TransferStrategy> transferSuggestions
) {}