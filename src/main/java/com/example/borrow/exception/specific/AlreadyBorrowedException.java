package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

import java.util.UUID;

public class AlreadyBorrowedException extends ConflictException {
    public AlreadyBorrowedException(UUID bookInstanceId) {
        super("Book instance "+bookInstanceId+" already borrowed");
    }
}