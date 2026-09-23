package com.tomachocolate.api.service;

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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ExpenseRepository expenseRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Participant createParticipant(UUID meetingId, String name, boolean includeInAllExpenses) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("Juntada no encontrada"));

        if (meeting.getParticipantCount() >= 30) {
            throw new BadRequestException("Máximo de 30 participantes");
        }

        String cleanName = name != null ? name.trim().replaceAll("^\"|\"$", "") : "";
        if (cleanName.isBlank()) {
            throw new BadRequestException("Ingrese un nombre válido");
        }

        int previousParticipantCount = meeting.getParticipants().size();

        // 1. Guardar primero el participante para asignarle ID en la base de datos
        Participant p = new Participant();
        p.setName(cleanName);
        p.setMeeting(meeting);
        Participant savedParticipant = participantRepository.save(p);

        // 2. Asociar a la reunión y actualizar el contador
        meeting.addParticipant(savedParticipant);
        meeting.setParticipantCount(meeting.getParticipants().size());

        // 3. Si corresponde, agregar a los gastos de "todos"
        if (includeInAllExpenses && previousParticipantCount > 0) {
            for (Expense expense : meeting.getExpenses()) {
                if (expense.getConsumers().size() == previousParticipantCount) {
                    expense.getConsumers().add(savedParticipant);
                }
            }
        }

        return savedParticipant;
    }

    @Transactional
    public void updateName(Long id, String newName) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El participante no existe"));

        String cleanName = newName != null ? newName.trim().replaceAll("^\"|\"$", "") : "";
        participant.setName(cleanName);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El participante no existe"));

        Meeting meeting = participant.getMeeting();

        if (meeting.getParticipantCount() <= 2) {
            throw new BadRequestException("Mínimo 2 participantes");
        }

        List<Expense> paidExpenses = expenseRepository.findByPayer(participant);
        if (!paidExpenses.isEmpty()) {
            expenseRepository.deleteAll(paidExpenses);
        }

        List<Expense> consumedExpenses = expenseRepository.findByConsumersContaining(participant);
        for (Expense expense : consumedExpenses) {
            expense.getConsumers().remove(participant);

            if (expense.getConsumers().isEmpty()) {
                expenseRepository.delete(expense);
            }
        }

        if (meeting.getParticipants() != null) {
            meeting.getParticipants().remove(participant);
        }
        meeting.setParticipantCount(meeting.getParticipantCount() - 1);

        participantRepository.delete(participant);
    }
}