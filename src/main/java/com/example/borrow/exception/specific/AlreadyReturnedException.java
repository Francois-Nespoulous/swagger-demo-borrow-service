package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

import java.util.UUID;

public class AlreadyReturnedException extends ConflictException {
    public AlreadyReturnedException(UUID bookInstanceId) {
        super("Book instance "+bookInstanceId+" already returned");
    }
}