package com.tomachocolate.api.controller;

import com.tomachocolate.api.dto.MeetingRequest;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Meeting create(@Valid @RequestBody MeetingRequest request){
        String name = request.name();
        int participantCount = request.participantCount();
        return meetingService.createMeeting(name, participantCount);
    }
}
