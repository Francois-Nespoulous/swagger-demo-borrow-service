package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.NotFoundException;

import java.util.UUID;

public class BookInstanceNotFoundException extends NotFoundException {
    public BookInstanceNotFoundException(UUID bookInstanceId) {
        super("BookInstance "+bookInstanceId+" not found");
    }
}
