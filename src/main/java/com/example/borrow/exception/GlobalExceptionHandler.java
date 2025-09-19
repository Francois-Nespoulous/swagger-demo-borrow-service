package com.example.borrow.exception;

import com.example.borrow.exception.global.ConflictException;
import com.example.borrow.exception.global.ForbiddenException;
import com.example.borrow.exception.global.NotFoundException;
import com.example.borrow.exception.global.RemoteServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.application.name}")
    private String serviceName;

    @ExceptionHandler(RemoteServiceException.class)
    public ResponseEntity<ErrorResponse> handleRemoteServiceException(RemoteServiceException ex) {
        return ResponseEntity
                .status(ex.getErrorResponse().status())
                .body(ex.getErrorResponse());
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return this.handleException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleLimitReachedException(ConflictException ex) {
        return this.handleException(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        return this.handleException(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherException(Exception ex) {
        return this.handleException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ignoredEx) {
        return this.handleException("You must be logged in to perform this operation", HttpStatus.UNAUTHORIZED);
    }
//TODO trouver pour differencier error admin de error not loggin
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ignoredEx) {
        return this.handleException("You do not have permission to access this resource", HttpStatus.FORBIDDEN);
    }


    private ResponseEntity<ErrorResponse> handleException(String message, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        serviceName, status.value(), message, LocalDateTime.now()
                ));
    }
}
