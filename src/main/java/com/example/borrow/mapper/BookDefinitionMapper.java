package com.example.borrow.mapper;
import com.example.borrow.domain.model.BookDefinition;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.dto.in.BookDefinitionClientDto;
import com.example.borrow.dto.out.BookDefinitionDto;
import com.example.borrow.dto.out.BookInstanceDto;

import java.util.List;

public class BookDefinitionMapper {
    public static BookDefinitionDto toDto(BookDefinition bookDefinition) {
        List<BookInstanceDto> instanceDtos = bookDefinition.getBookInstances().stream()
                .map(bookInstance -> BookInstanceMapper.toDto(bookInstance, null))
                .toList();

        BookDefinitionDto bookDefinitionDto = BookDefinitionMapper.toDto(bookDefinition, instanceDtos);

        instanceDtos.forEach(bookInstance -> bookInstance.setBookDefinitionDto(bookDefinitionDto));

        return bookDefinitionDto;
    }

    private static BookDefinitionDto toDto(BookDefinition bookDefinition, List<BookInstanceDto> bookInstanceDtos) {
        return new BookDefinitionDto(
                bookDefinition.getId(),
                bookDefinition.getTitle(),
                bookDefinition.getAuthor(),
                bookDefinition.getNbTotalBooks(),
                bookDefinition.getNbBorrowedBooks(),
                bookInstanceDtos
        );
    }

    public static BookDefinition toDomain(BookDefinitionClientDto dto, List<BookInstance> instances, int nbTotal, int nbBorrowed) {
        return new BookDefinition(
                dto.id(),
                dto.title(),
                dto.author(),
                instances,
                nbTotal,
                nbBorrowed
        );
    }
}
