package com.tomachocolate.api.repository;

import com.tomachocolate.api.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    Long deleteByCreatedAtBefore(LocalDateTime expiryDate);
    Meeting getMeetingById(UUID meetingId);

    @Modifying
    @Query(value = "UPDATE app_stats SET total_meetings_created = total_meetings_created + 1 WHERE id = 1", nativeQuery = true)
    void incrementTotalMeetings();

    @Query(value = "SELECT total_meetings_created FROM app_stats WHERE id = 1", nativeQuery = true)
    Long getTotalMeetingsCount();
}