package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.NotFoundException;

public class LoggedUserNotFoundException extends NotFoundException {
    public LoggedUserNotFoundException() {
        super("Logged user not found");
    }
}