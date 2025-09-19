package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ConflictException;

public class TooMuchBooksBorrowedForUserException extends ConflictException {
    public TooMuchBooksBorrowedForUserException(String username) {
        super("Too much books nbBorrowedBooks for user "+username);
    }
}