package com.example.borrow.repository;

import com.example.borrow.persistence.repository.entity.LockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public interface LockRepository extends JpaRepository<LockEntity, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM LockEntity l WHERE l.lockedAt < :expirationTime")
    int deleteExpiredLocks(Instant expirationTime);

    default int deleteExpired(Duration duration) {
        return deleteExpiredLocks(Instant.now().minus(duration));
    }

    void deleteByBookInstanceId(UUID bookInstanceId);

    boolean existsByBookInstanceId(UUID bookInstanceId);

    boolean existsByUserId(UUID userId);
}
