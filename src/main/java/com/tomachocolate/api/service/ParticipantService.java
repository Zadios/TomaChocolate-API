package com.tomachocolate.api.service;

import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantRepository participantRepository;

    @Transactional
    public void updateName(Long id, String newName) {
        Participant participant = participantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Participante no encontrado"));

        participant.setName(newName);
        participantRepository.save(participant);
    }

}
