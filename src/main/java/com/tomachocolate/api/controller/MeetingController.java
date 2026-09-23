package com.tomachocolate.api.controller;

import com.tomachocolate.api.dto.MeetingBalanceResponse;
import com.tomachocolate.api.dto.MeetingRequest;
import com.tomachocolate.api.model.Meeting;
import com.tomachocolate.api.service.BalanceService;
import com.tomachocolate.api.service.MeetingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;
    private final BalanceService balanceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Meeting create(@Valid @RequestBody MeetingRequest request) {
        return meetingService.createMeeting(request.name(), request.participantCount());
    }

    @GetMapping("/{id}")
    public Meeting getMeetingById(@PathVariable UUID id) {
        return meetingService.getMeeting(id);
    }

    @GetMapping("/stats/total-meetings")
    public ResponseEntity<Map<String, Long>> getTotalMeetings() {
        return ResponseEntity.ok(Collections.singletonMap("total", meetingService.getTotalMeetingsCount()));
    }

    @GetMapping("/{id}/balance")
    public MeetingBalanceResponse getBalance(@PathVariable UUID id) {
        return balanceService.calculateBalance(id);
    }
}