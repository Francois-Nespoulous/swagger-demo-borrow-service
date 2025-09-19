package com.example.borrow.domain.model;

import com.example.borrow.domain.enums.BookState;

import java.time.Instant;
import java.util.UUID;

public class BookInstance {
    private UUID id;

    private BookState bookState;

    private BookDefinition bookDefinition;

    private boolean borrowed;

    private Long version;

    private Instant lastBorrowAttempt;


    public BookInstance(UUID id, BookState bookState, BookDefinition bookDefinitionSummary, boolean borrowed, Long version, Instant lastBorrowAttempt) {
        this.id = id;
        this.bookState = bookState;
        this.bookDefinition = bookDefinitionSummary;
        this.borrowed = borrowed;
        this.version = version;
        this.lastBorrowAttempt = lastBorrowAttempt;
    }

    public UUID getId() {
        return id;
    }

    public BookState getBookState() {
        return bookState;
    }

    public BookDefinition getBookDefinition() {
        return bookDefinition;
    }

    public boolean isBorrowed() {
        return borrowed;
    }

    public void setBookDefinition(BookDefinition bookDefinition) {
        this.bookDefinition = bookDefinition;
    }

    public void updateLastAttempt() {
        this.lastBorrowAttempt = Instant.now();
    }

    public Long getVersion() {
        return version;
    }
}
