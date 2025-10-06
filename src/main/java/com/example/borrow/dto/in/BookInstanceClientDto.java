package com.example.borrow.dto.in;

import com.example.borrow.domain.enums.BookState;

import java.util.UUID;

public record BookInstanceClientDto(
        UUID id,
        BookState bookState,

        BookDefinitionClientDto bookDefinition
) {
}
//TODO controller/dto pour out
//TODO service/ext pour in