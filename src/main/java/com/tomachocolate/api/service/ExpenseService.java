package com.tomachocolate.api.service;

import com.tomachocolate.api.dto.ExpenseRequest;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.ExpenseRepository;
import com.tomachocolate.api.repository.MeetingRepository;
import com.tomachocolate.api.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Expense addExpense(ExpenseRequest expenseRequest) {
        Meeting meeting = meetingRepository.findById(expenseRequest.meetingId())
                .orElseThrow(() -> new RuntimeException("No se encontró la juntada"));
        Participant payer = participantRepository.findById(expenseRequest.payerId())
                .orElseThrow(() -> new RuntimeException("No se encontró el participante"));

        if (!meeting.getId().equals(payer.getMeeting().getId())) {
            throw new RuntimeException("El participante no pertenece a esta juntada");
        }

        Expense expense = Expense.builder()
                .meeting(meeting)
                .payer(payer)
                .description(expenseRequest.description())
                .amount(expenseRequest.amount())
                .build();

        meeting.getExpenses().add(expense);
        return expenseRepository.save(expense);
    }
}
