package com.example.borrow.scheduler;

import com.example.borrow.repository.LockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class BookInstanceLockCleanupTask {
    private final LockRepository lockRepository;
    private static final Logger log = LoggerFactory.getLogger(BookInstanceLockCleanupTask.class);

    public BookInstanceLockCleanupTask(LockRepository lockRepository) {
        this.lockRepository = lockRepository;
    }

    @Scheduled(fixedDelay = 60_000)
    public void cleanupExpiredLocks() {
        int nbLocksDeleted = lockRepository.deleteExpired(Duration.ofMinutes(1));
        if (nbLocksDeleted > 0) {
            log.info(nbLocksDeleted + " expired locks deleted");
        }
    }
}
