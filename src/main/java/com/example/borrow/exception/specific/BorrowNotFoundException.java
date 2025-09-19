package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.NotFoundException;

import java.util.UUID;

public class BorrowNotFoundException extends NotFoundException {
    public BorrowNotFoundException(UUID borrowId) {
        super("Borrow "+borrowId+" not found");
    }
}