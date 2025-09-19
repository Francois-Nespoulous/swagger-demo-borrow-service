package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

import java.util.UUID;

public class ConcurrentReturnException extends ConflictException {
    public ConcurrentReturnException(UUID bookInstanceId) {
        super("Another user is already returning book instance "+bookInstanceId);
    }
}