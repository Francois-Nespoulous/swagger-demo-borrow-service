package com.example.borrow.dto.in;

import com.example.borrow.domain.enums.BookState;

import java.util.UUID;

//TODO pas besoin normalement, manque annot jackson pour recup
public record BookInstanceSimpleClientDto(
        UUID id,
        BookState bookState,

        UUID bookDefinition
) {
}
