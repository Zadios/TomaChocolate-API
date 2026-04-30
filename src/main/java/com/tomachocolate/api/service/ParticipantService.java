package com.tomachocolate.api.service;

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

        Participant p = new Participant();
        p.setName(name);
        meeting.addParticipant(p);
        meeting.setParticipantCount(meeting.getParticipantCount() +1);
        return participantRepository.save(p);
        }

    @Transactional
    public void updateName(Long id, String newName) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No pudimos encontrar al participante con ID: " + id));

        participant.setName(newName);
        participantRepository.save(participant);
    }

    @Transactional
    public void deleteParticipant(Long id) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No pudimos encontrar al participante con ID: " + id));

        Meeting meeting = participant.getMeeting();

        expenseRepository.deleteByPayer(participant);
        participantRepository.delete(participant);
        meeting.setParticipantCount(meeting.getParticipantCount() - 1);
    }

}
