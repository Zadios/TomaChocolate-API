package com.tomachocolate.api.service;

import com.tomachocolate.api.exception.BadRequestException;
import com.tomachocolate.api.exception.ResourceNotFoundException;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.ExpenseRepository;
import com.tomachocolate.api.repository.MeetingRepository;
import com.tomachocolate.api.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;
    private final ExpenseRepository expenseRepository;
    private final MeetingRepository meetingRepository;

    @Transactional
    public Participant createParticipant(UUID meetingId, String name){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("Juntada no encontrada"));

        if(meeting.getParticipantCount() == 30){
            throw new BadRequestException("Máximo de 30 participantes");
        }

        if(name == null || name.isBlank()){
            throw new BadRequestException("Ingrese un nombre válido");
        } else {
            Participant p = new Participant();
            p.setName(name);
            meeting.addParticipant(p);
            meeting.setParticipantCount(meeting.getParticipantCount() +1);
            return participantRepository.save(p);
        }}

    @Transactional
    public void updateName(Long id, String newName) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El participante no existe"));

        participant.setName(newName);
        participantRepository.save(participant);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El participante no existe"));

        Meeting meeting = participant.getMeeting();

        if (meeting.getParticipantCount() <= 2) {
            throw new BadRequestException("Mínimo 2 participantes");
        } else {
            expenseRepository.deleteByPayer(participant);
            participantRepository.delete(participant);
            meeting.setParticipantCount(meeting.getParticipantCount() - 1);
        }
    }

}
