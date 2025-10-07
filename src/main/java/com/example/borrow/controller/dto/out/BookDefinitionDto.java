package com.example.borrow.controller.dto.out;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.List;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public record BookDefinitionDto(
        UUID id,
        String title,
        String author,
        int nbTotalBooks,
        int nbBorrowedBooks,

        List<BookInstanceDto> bookInstanceDtos
) {
}