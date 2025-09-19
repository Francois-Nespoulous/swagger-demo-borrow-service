package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.NotFoundException;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID userId) {
        super("User "+userId+" not found");
    }
}