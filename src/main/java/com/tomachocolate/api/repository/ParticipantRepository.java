package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}