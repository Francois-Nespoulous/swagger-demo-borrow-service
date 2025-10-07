package com.example.borrow.persistence.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "book_instance_lock")
public class LockEntity {
    @Id
    private UUID bookInstanceId;

    private UUID userId;

    private Instant lockedAt;

    public LockEntity() {
    }

    public LockEntity(UUID bookInstanceId, UUID userId, Instant lockedAt) {
        this.bookInstanceId = bookInstanceId;
        this.userId = userId;
        this.lockedAt = lockedAt;
    }
}
