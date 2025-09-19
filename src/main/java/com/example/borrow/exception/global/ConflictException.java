package com.example.borrow.exception.global;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
