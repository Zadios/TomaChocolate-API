package com.tomachocolate.api.service;

import com.tomachocolate.api.exception.ResourceNotFoundException;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;

    @Transactional
    public Meeting createMeeting(String name, int participantCount){
        Meeting meeting = new Meeting();
        meeting.setName(name);
        meeting.setParticipantCount(participantCount);

        for (int i = 1; i <= participantCount; i++){
            Participant p = new Participant();
            p.setName("Usuario " + i);
            meeting.addParticipant(p);
        }

        return meetingRepository.save(meeting);
    }

    public Meeting getMeeting(UUID meetingId){
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la juntada"));
    }
}
