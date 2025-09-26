package com.example.borrow.mapper;

import com.example.borrow.domain.model.BookDefinition;
import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.dto.out.BookDefinitionDto;
import com.example.borrow.persistence.repository.entity.BookDefinitionEntity;
import com.example.borrow.repository.BorrowRepository;
import com.example.borrow.util.BookStatsUtil;

import java.util.List;

public class BookDefinitionMapper {
    public static BookDefinition toDomainWithoutInstance(BookDefinitionEntity bookDefinitionEntity, BorrowRepository borrowRepository) {
        BookStatsUtil.BookStats bookStats = BookStatsUtil.computeStats(bookDefinitionEntity.getBookInstances(), borrowRepository);

        return new BookDefinition(
                bookDefinitionEntity.getId(),
                bookDefinitionEntity.getTitle(),
                bookDefinitionEntity.getAuthor(),
                null,
                bookStats.nbTotalBooks(),
                bookStats.nbBorrowedBooks()
        );
    }

    public static BookDefinition toDomain(BookDefinitionEntity bookDefinitionEntity, BorrowRepository borrowRepository) {
        BookStatsUtil.BookStats bookStats = BookStatsUtil.computeStats(bookDefinitionEntity.getBookInstances(), borrowRepository);

        List<BookInstance> bookInstances = bookDefinitionEntity.getBookInstances()
                .stream()
                .map(bookInstanceEntity -> BookInstanceMapper.toDomainWithoutDefinition(bookInstanceEntity, borrowRepository))
                .toList();

        BookDefinition bookDefinition = new BookDefinition(
                bookDefinitionEntity.getId(),
                bookDefinitionEntity.getTitle(),
                bookDefinitionEntity.getAuthor(),
                bookInstances,
                bookStats.nbTotalBooks(),
                bookStats.nbBorrowedBooks()
        );

        for (BookInstance bookInstance : bookInstances) {
            bookInstance.setBookDefinition(bookDefinition);
        }

        return bookDefinition;
    }


    public static BookDefinitionDto toDtoWithoutInstance(BookDefinition bookDefinition) {
        return new BookDefinitionDto(
                bookDefinition.getId(),
                bookDefinition.getTitle(),
                bookDefinition.getAuthor(),
                bookDefinition.getNbTotalBooks(),
                bookDefinition.getNbBorrowedBooks(),
                null
        );
    }

    public static BookDefinitionDto toDto(BookDefinition bookDefinition) {
        return toDto(bookDefinition, false);
    }

    //TODO check avec les annotations https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
    public static BookDefinitionDto toDto(BookDefinition bookDefinition, boolean showOnlyAvailableBooks) {
        return new BookDefinitionDto(
                bookDefinition.getId(),
                bookDefinition.getTitle(),
                bookDefinition.getAuthor(),
                bookDefinition.getNbTotalBooks(),
                bookDefinition.getNbBorrowedBooks(),
                bookDefinition.getBookInstances()
                        .stream()
                        .filter(bookInstance -> !showOnlyAvailableBooks || !bookInstance.isBorrowed())
                        .map(BookInstanceMapper::toDtoWithoutDefinition)
                        .toList()
        );
    }

    public static BookDefinitionEntity toEntityOnlyId(BookDefinition bookDefinition) {
        BookDefinitionEntity bookDefinitionEntity = new BookDefinitionEntity();
        bookDefinitionEntity.setId(bookDefinition.getId());
        return bookDefinitionEntity;
    }

    public static BookDefinition toDomainWithoutInstance(BookDefinitionDto bookDefinitionDto) {
        return new BookDefinition(
                bookDefinitionDto.id(),
                bookDefinitionDto.title(),
                bookDefinitionDto.author(),
                null,
                bookDefinitionDto.nbTotalBooks(),
                bookDefinitionDto.nbBorrowedBooks()
        );
    }
}
