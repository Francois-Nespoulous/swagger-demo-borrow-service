package com.example.borrow.exception.specific;

import com.example.borrow.exception.global.ForbiddenException;

public class MustBeLoginInException extends ForbiddenException {
    public MustBeLoginInException() {
        super("You must be logged in to perform this operation");
    }
}
