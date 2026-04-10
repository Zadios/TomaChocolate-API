package com.tomachocolate.api.service;

import com.tomachocolate.api.dto.MeetingBalanceResponse;
import com.tomachocolate.api.dto.ParticipantBalance;
import com.tomachocolate.api.dto.TransferStrategy;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BalanceService {
    private final MeetingRepository meetingRepository;

    public MeetingBalanceResponse calculateBalance(UUID meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("No se encontró la juntada"));

        BigDecimal total = meeting.getExpenses().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal average = total.divide(BigDecimal.valueOf(meeting.getParticipantCount()), 2, RoundingMode.HALF_UP);
        List<ParticipantBalance> balances = new ArrayList<>();

        Map<Long, BigDecimal> totalsByParticipant = meeting.getExpenses().stream()
                .collect(Collectors.groupingBy(
                        expense -> expense.getPayer().getId(),
                        Collectors.mapping(Expense::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));

        for (Participant participant : meeting.getParticipants()) {
            BigDecimal amountPaid = totalsByParticipant.getOrDefault(participant.getId(), BigDecimal.ZERO);

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
                .transferSuggestions(generateTransferSuggestions(balances))
                .build();
    }

    private List<TransferStrategy> generateTransferSuggestions(List<ParticipantBalance> balances) {
        List<MutableBalance> debtors = new ArrayList<>();
        List<MutableBalance> creditors = new ArrayList<>();
        List<TransferStrategy> suggestions = new ArrayList<>();

        for (ParticipantBalance b : balances) {
            if (b.balance().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(new MutableBalance(b.name(), b.balance().abs()));
            } else if (b.balance().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(new MutableBalance(b.name(), b.balance()));
            }
        }

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            MutableBalance debtor = debtors.get(0);
            MutableBalance creditor = creditors.get(0);

            BigDecimal amount = debtor.remainingAmount.min(creditor.remainingAmount);
            suggestions.add(new TransferStrategy(debtor.name, creditor.name, amount));

            debtor.remainingAmount = debtor.remainingAmount.subtract(amount);
            creditor.remainingAmount = creditor.remainingAmount.subtract(amount);

            if (debtor.remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                debtors.remove(0);
            }

            if (creditor.remainingAmount.compareTo(BigDecimal.ZERO) == 0) {
                creditors.remove(0);
            }
        }
        return suggestions;

    }

    private static class MutableBalance {
        final String name;
        BigDecimal remainingAmount;

        public MutableBalance(String name, BigDecimal amount) {
            this.name = name;
            this.remainingAmount = amount;
        }
    }
}
