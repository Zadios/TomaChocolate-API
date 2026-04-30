package com.tomachocolate.api.controller;

import com.tomachocolate.api.dto.MeetingBalanceResponse;
import com.tomachocolate.api.dto.MeetingRequest;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.service.BalanceService;
import com.tomachocolate.api.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {
    private final MeetingService meetingService;
    private final BalanceService balanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Meeting create(@Valid @RequestBody MeetingRequest request){
        String name = request.name();
        int participantCount = request.participantCount();
        return meetingService.createMeeting(name, participantCount);
    }

    @GetMapping("/{id}")
    public Meeting getMeetingById(@PathVariable UUID id) {
        return meetingService.getMeeting(id);
    }

    @GetMapping("/{id}/balance")
    public MeetingBalanceResponse getBalance(@PathVariable UUID id) {
        return balanceService.calculateBalance(id);
    }
}
