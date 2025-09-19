package com.example.borrow.dto.out;

import com.example.borrow.domain.enums.BookState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BookInstanceDto(
        UUID id,
        BookState bookState,

        @JsonIgnoreProperties("bookInstance")
        BookDefinitionDto bookDefinition,
        boolean borrowed
) {
}
