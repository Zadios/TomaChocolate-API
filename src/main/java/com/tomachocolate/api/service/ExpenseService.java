package com.tomachocolate.api.service;

import com.tomachocolate.api.dto.ExpenseRequest;
import com.tomachocolate.api.exception.BadRequestException;
import com.tomachocolate.api.exception.ResourceNotFoundException;
import com.tomachocolate.api.model.Expense;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.ExpenseRepository;
import com.tomachocolate.api.repository.MeetingRepository;
import com.tomachocolate.api.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final ParticipantRepository participantRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Expense addExpense(UUID meetingId, ExpenseRequest expenseRequest) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la juntada"));

        Participant payer = participantRepository.findById(expenseRequest.payerId())
                .orElseThrow(() -> new ResourceNotFoundException("El participante no existe"));

        if (expenseRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto del gasto debe ser mayor a cero.");
        }

        if (!meeting.getId().equals(payer.getMeeting().getId())) {
            throw new BadRequestException("El participante no pertenece a esta juntada");
        }

        Set<Participant> consumers = resolveConsumers(meeting, expenseRequest.consumerIds());

        Expense expense = Expense.builder()
                .meeting(meeting)
                .payer(payer)
                .description(expenseRequest.description())
                .amount(expenseRequest.amount())
                .consumers(consumers)
                .build();

        meeting.getExpenses().add(expense);
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(UUID meetingId, Long expenseId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("No se encontró la juntada"));

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("El gasto no existe"));

        if (!expense.getMeeting().getId().equals(meetingId)) {
            throw new RuntimeException("El gasto no pertenece a esta juntada");
        }

        meeting.getExpenses().remove(expense);
        expenseRepository.delete(expense);
    }

    @Transactional
    public void updateExpense(Long expenseId, ExpenseRequest expenseRequest) {
        if (expenseRequest.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("El monto del gasto debe ser mayor a cero.");
        }

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("El gasto no existe"));

        expense.setAmount(expenseRequest.amount());
        expense.setDescription(expenseRequest.description());

        Participant newPayer = participantRepository.findById(expenseRequest.payerId())
                .orElseThrow(() -> new ResourceNotFoundException("El nuevo pagador no existe"));

        expense.setPayer(newPayer);

        Set<Participant> consumers = resolveConsumers(expense.getMeeting(), expenseRequest.consumerIds());
        expense.setConsumers(consumers);
    }

    private Set<Participant> resolveConsumers(Meeting meeting, java.util.List<Long> consumerIds) {
        if (consumerIds == null || consumerIds.isEmpty()) {
            return new HashSet<>(meeting.getParticipants());
        }

        Set<Participant> consumers = meeting.getParticipants().stream()
                .filter(p -> consumerIds.contains(p.getId()))
                .collect(Collectors.toSet());

        if (consumers.isEmpty()) {
            throw new BadRequestException("Debe seleccionar al menos un consumidor válido para el gasto.");
        }

        return consumers;
    }
}