package com.tomachocolate.api.service;

import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.model.Participant;
import com.tomachocolate.api.repository.MeetingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
            p.setName("Amigo " + i);
            meeting.addParticipant(p);
        }

        return meetingRepository.save(meeting);
    }
}
