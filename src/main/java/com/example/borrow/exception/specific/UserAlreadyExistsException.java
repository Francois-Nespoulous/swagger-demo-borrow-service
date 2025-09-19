package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

public class UserAlreadyExistsException extends ConflictException {
    public UserAlreadyExistsException(String username) {
        super("User "+username+" already exists");
    }
}