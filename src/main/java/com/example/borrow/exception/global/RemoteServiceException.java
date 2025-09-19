package com.example.borrow.exception.global;

import com.example.borrow.exception.ErrorResponse;

public class RemoteServiceException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public RemoteServiceException(ErrorResponse errorResponse) {
        super(errorResponse.message());
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}