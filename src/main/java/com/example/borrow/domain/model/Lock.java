package com.example.borrow.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Lock {
    private UUID bookInstanceId;
    private UUID userId;
    private Instant lockedAt;

    public Lock(UUID bookInstanceId, UUID userId, Instant lockedAt) {
        this.bookInstanceId = bookInstanceId;
        this.userId = userId;
        this.lockedAt = lockedAt;
    }

    public UUID getBookInstanceId() {
        return bookInstanceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }
}
