package com.tomachocolate.api.controller;

import com.tomachocolate.api.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateName(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        String newName = body.get("name");
        participantService.updateName(id, newName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteParticipant(@PathVariable Long id){
        participantService.deleteParticipant(id);
    }
}
