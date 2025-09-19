package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ForbiddenException;

public class InvalidCredentialsException extends ForbiddenException {
    public InvalidCredentialsException(String username) {
        super("Invalid credentials for user "+username);
    }
}