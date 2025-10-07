package com.example.borrow.domain.service.ext;

import com.example.borrow.domain.enums.BookState;

import java.util.UUID;

//TODO pas besoin normalement, manque annot jackson pour recup
public record BookInstanceSimpleClientDto(
        UUID id,
        BookState bookState,

        UUID bookDefinition
) {
}
