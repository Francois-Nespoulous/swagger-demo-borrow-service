package com.example.borrow.persistence.repository.entity;

import com.example.borrow.domain.enums.BookState;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "book_instance")
public class BookInstanceEntity {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", updatable = false, nullable = false, length = 36)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private BookState bookState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_definition_id")
    private BookDefinitionEntity bookDefinition;

    @Version
    private Long version;
    private Instant lastBorrowAttempt;

    public BookInstanceEntity() {}

    public BookInstanceEntity(UUID id, BookState bookState, BookDefinitionEntity bookDefinitionEntity, Long version) {
        this.id = id;
        this.bookState = bookState;
        this.bookDefinition = bookDefinitionEntity;
        this.version = version;
    }

    public UUID getId() {
        return id;
    }

    public BookState getBookState() {
        return bookState;
    }

    public BookDefinitionEntity getBookDefinition() {
        return bookDefinition;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        System.out.printf("Set version to %d\n", version);
        this.version = version;
    }

    public Instant getLastBorrowAttempt() {
        return lastBorrowAttempt;
    }
}
