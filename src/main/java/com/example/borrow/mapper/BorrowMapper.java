package com.example.borrow.mapper;

import com.example.borrow.domain.model.BookInstance;
import com.example.borrow.domain.model.Borrow;
import com.example.borrow.domain.model.User;
import com.example.borrow.dto.out.BorrowDto;
import com.example.borrow.persistence.repository.entity.BorrowEntity;

public class BorrowMapper {
    public static Borrow toDomain(BorrowEntity borrowEntity, BookInstance bookInstance, User user) {
        return new Borrow(
                borrowEntity.getId(),
                bookInstance,
                user,
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
                borrow.getBookInstance().getId(),
                borrow.getUser().getId(),
                borrow.getStatus(),
                borrow.getBorrowDate(),
                borrow.getReturnDate()
        );
    }
}
