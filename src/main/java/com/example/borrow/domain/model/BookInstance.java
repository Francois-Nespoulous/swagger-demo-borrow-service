package com.example.borrow.domain.model;

import com.example.borrow.domain.enums.BookState;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.UUID;

public class BookInstance {
    private UUID id;

    private BookState bookState;

    @JsonBackReference
    private BookDefinition bookDefinition;

    private boolean borrowed;


    public BookInstance(UUID id, BookState bookState, BookDefinition bookDefinitionSummary, boolean borrowed) {
        this.id = id;
        this.bookState = bookState;
        this.bookDefinition = bookDefinitionSummary;
        this.borrowed = borrowed;
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

    public void setBorrowed(boolean borrowed) {
        this.borrowed = borrowed;
    }
}
