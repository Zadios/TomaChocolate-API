package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Long deleteByCreatedAtBefore(LocalDateTime expiryDate);
}