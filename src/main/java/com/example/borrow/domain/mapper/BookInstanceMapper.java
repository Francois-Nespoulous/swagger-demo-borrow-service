package com.example.borrow.domain.mapper;

import com.example.borrow.domain.model.BookDefinition;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.domain.service.ext.BookInstanceClientDto;
import com.example.borrow.domain.service.ext.BookInstanceSimpleClientDto;
import com.example.borrow.controller.dto.out.BookDefinitionDto;
import com.example.borrow.controller.dto.out.BookInstanceDto;

public class BookInstanceMapper {
    public static BookInstanceDto toDto(BookInstance bookInstance) {
        BookDefinitionDto bookDefinitionDto = BookDefinitionMapper.toDto(bookInstance.getBookDefinition());
        return BookInstanceMapper.toDto(bookInstance, bookDefinitionDto);
    }

    static BookInstanceDto toDto(BookInstance bookInstance, BookDefinitionDto bookDefinitionDto) {
        return new BookInstanceDto(
                bookInstance.getId(),
                bookInstance.getBookState(),
                bookDefinitionDto,
                bookInstance.isBorrowed()
        );
    }

    public static BookInstance toDomain(BookInstanceClientDto dto, BookDefinition parent, boolean borrowed) {
        return new BookInstance(
                dto.id(),
                dto.bookState(),
                parent,
                borrowed
        );
    }

    public static BookInstance toDomain(BookInstanceSimpleClientDto dto, BookDefinition parent, boolean borrowed) {
        return new BookInstance(
                dto.id(),
                dto.bookState(),
                parent,
                borrowed
        );
    }
}
