package com.example.borrow.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        String service,
        int status,
        String message,
        LocalDateTime timestamp
) {}