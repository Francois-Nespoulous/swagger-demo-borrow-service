package com.example.borrow.dto.out;

import com.example.borrow.domain.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record BorrowDto(UUID id, BookInstanceDto bookInstanceDto, UserDto userDto, BorrowStatus status, LocalDateTime borrowDate, LocalDateTime returnDate) {
}
