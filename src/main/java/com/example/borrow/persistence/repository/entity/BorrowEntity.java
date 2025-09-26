package com.example.borrow.persistence.repository.entity;

import com.example.borrow.domain.enums.BorrowStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "borrow")
public class BorrowEntity {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private UUID id;

    private UUID bookInstanceId;

    @JoinColumn(name = "username") //TODO remane en userId, mais utilisent la meme db
    private UUID userId;

    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;

    public BorrowEntity() {
    }

    public BorrowEntity(UUID id, UUID bookInstanceEntity, UUID userEntity, BorrowStatus status, LocalDateTime borrowDate, LocalDateTime returnDate) {
        this.id = id;
        this.bookInstanceId = bookInstanceEntity;
        this.userId = userEntity;
        this.status = status;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getBookInstanceId() {
        return bookInstanceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime localDateTime) {
        this.returnDate = localDateTime;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }
}
