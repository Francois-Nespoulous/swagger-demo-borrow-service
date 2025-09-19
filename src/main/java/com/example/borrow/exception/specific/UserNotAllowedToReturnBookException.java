package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ForbiddenException;

public class UserNotAllowedToReturnBookException extends ForbiddenException {
    public UserNotAllowedToReturnBookException(String usernameWantToReturn) {
        super("User " + usernameWantToReturn + " not allowed to return this book");
    }
}