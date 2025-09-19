package com.example.borrow.mapper;

import com.example.borrow.domain.model.Borrow;
import com.example.borrow.dto.out.BorrowDto;
import com.example.borrow.persistence.repository.entity.BorrowEntity;
import com.example.borrow.repository.BorrowRepository;

public class BorrowMapper {
    public static Borrow toDomain(BorrowEntity borrowEntity, BorrowRepository borrowRepository) {
        return new Borrow(
                borrowEntity.getId(),
                BookInstanceMapper.toDomain(borrowEntity.getBookInstance(), borrowRepository),
                UserMapper.toDomain(borrowEntity.getUser(), borrowRepository),
                borrowEntity.getStatus(),
                borrowEntity.getBorrowDate(),
                borrowEntity.getReturnDate()
        );
    }

    public static BorrowDto toDto(Borrow borrow) {
        return new BorrowDto(
                borrow.getId(),
                BookInstanceMapper.toDto(borrow.getBookInstance()),
                UserMapper.toDto(borrow.getUser()),
                borrow.getStatus(),
                borrow.getBorrowDate(),
                borrow.getReturnDate()
        );
    }

    public static BorrowEntity toEntity(Borrow borrow) {
        return new BorrowEntity(
                borrow.getId(),
                BookInstanceMapper.toEntity(borrow.getBookInstance()),
                UserMapper.toEntity(borrow.getUser(), null),
                borrow.getStatus(),
                borrow.getBorrowDate(),
                borrow.getReturnDate()
        );
    }
}
