package com.tomachocolate.api.service;

import com.tomachocolate.api.dto.MeetingBalanceResponse;
import com.tomachocolate.api.dto.ParticipantBalance;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final MeetingRepository meetingRepository;

    public MeetingBalanceResponse calculateBalance(UUID meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("No se encontró la juntada"));

        BigDecimal total = BigDecimal.ZERO;
        for(Expense expense : meeting.getExpenses()){
            total = total.add(expense.getAmount());
        }

        BigDecimal average = total.divide(BigDecimal.valueOf(meeting.getParticipantCount()), 2, RoundingMode.HALF_UP);
        List<ParticipantBalance> balances = new ArrayList<>();

        for( Participant participant : meeting.getParticipants()){
            BigDecimal amountPaid = BigDecimal.ZERO;
            for (Expense expense : meeting.getExpenses()){
                if(Objects.equals(participant.getId(), expense.getPayer().getId())){
                    amountPaid = amountPaid.add(expense.getAmount());
                }
            }
            ParticipantBalance participantBalance = ParticipantBalance.builder()
                    .name(participant.getName())
                    .totalPaid(amountPaid)
                    .balance(amountPaid.subtract(average))
                    .build();
            balances.add(participantBalance);
        }

        return MeetingBalanceResponse.builder()
                .participantBalances(balances)
                .averagePerPerson(average)
                .totalAmount(total)
                .transferSuggestions(null)
                .build();
    }
}
