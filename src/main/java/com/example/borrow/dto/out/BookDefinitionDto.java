package com.example.borrow.dto.out;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookDefinitionDto(
        UUID id,
        String title,
        String author,
        int nbTotalBooks,
        int nbBorrowedBooks,

        @JsonIgnoreProperties("bookDefinition")
        List<BookInstanceDto> bookInstances
) {
}
//TODO check customization du mapper Jackson