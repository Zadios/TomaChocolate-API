package com.tomachocolate.api.service;

import com.tomachocolate.api.repository.MeetingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleanupService {

    private final MeetingRepository meetingRepository;

    @Scheduled(fixedRate = 10000) // Recordá volver a 86400000 después del test
    @Transactional
    public void autoPurgeOldMeetings() {
        LocalDateTime expirationLimit = LocalDateTime.now().minusMinutes(1);

        log.info("Iniciando purga de datos efímeros...");

        Long deletedCount = meetingRepository.deleteByCreatedAtBefore(expirationLimit);

        if (deletedCount > 0) {
            log.info("Limpieza completada: Se eliminaron {} reuniones antiguas con sus gastos y participantes.", deletedCount);
        } else {
            log.info("Limpieza completada: No se encontraron registros para eliminar.");
        }
    }
}