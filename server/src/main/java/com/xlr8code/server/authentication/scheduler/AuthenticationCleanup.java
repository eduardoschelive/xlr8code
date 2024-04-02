package com.xlr8code.server.authentication.scheduler;

import com.xlr8code.server.authentication.repository.UserCodeRepository;
import com.xlr8code.server.authentication.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class AuthenticationCleanup {

    private final UserCodeRepository userCodeRepository;
    private final UserSessionRepository userSessionRepository;

    @Scheduled(cron = "${authentication.cleanup-cron}")
    @Transactional
    public void cleanupAuthenticationTables() {
        userCodeRepository.deleteAllByExpiresAtBefore(Instant.now());
        userSessionRepository.deleteAllByExpiresAtBefore(Instant.now());
    }

}
