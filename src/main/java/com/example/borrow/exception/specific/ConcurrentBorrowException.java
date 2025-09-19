package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

import java.util.UUID;

public class ConcurrentBorrowException extends ConflictException {
    public ConcurrentBorrowException(UUID bookInstanceId) {
        super("Another user is already borrowing book instance "+bookInstanceId);
    }
}