package com.example.borrow.dto.in;

import java.util.List;
import java.util.UUID;

public record BookDefinitionClientDto(
        UUID id,
        String title,
        String author,

        List<BookInstanceSimpleClientDto> bookInstances
) {
}