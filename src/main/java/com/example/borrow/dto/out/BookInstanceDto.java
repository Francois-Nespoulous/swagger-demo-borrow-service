package com.example.borrow.dto.out;

import com.example.borrow.domain.enums.BookState;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookInstanceDto {
    private UUID id;
    private BookState bookState;

    private BookDefinitionDto bookDefinitionDto;
    private boolean borrowed;

    public BookInstanceDto(UUID id, BookState bookState, BookDefinitionDto bookDefinitionDto, boolean borrowed) {
        this.id = id;
        this.bookState = bookState;
        this.bookDefinitionDto = bookDefinitionDto;
        this.borrowed = borrowed;
    }

    public void setBookDefinitionDto(BookDefinitionDto bookDefinitionDto) {
        this.bookDefinitionDto = bookDefinitionDto;
    }

    public UUID getId() {
        return id;
    }

    public BookState getBookState() {
        return bookState;
    }

    public BookDefinitionDto getBookDefinitionDto() {
        return bookDefinitionDto;
    }

    public boolean isBorrowed() {
        return borrowed;
    }
}
