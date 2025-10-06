package com.example.borrow.domain.model;

import com.example.borrow.domain.enums.BorrowStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Borrow {
    private UUID id;

    private BookInstance bookInstance;
    private User user;

    private BorrowStatus status;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;

    public Borrow(UUID id, BookInstance bookInstance, User user, BorrowStatus status, LocalDateTime borrowDate, LocalDateTime returnDate) {
        this.id = id;
        this.bookInstance = bookInstance;
        this.user = user;
        this.status = status;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public UUID getId() {
        return id;
    }

    public BookInstance getBookInstance() {
        return bookInstance;
    }

    public User getUser() {
        return user;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }


    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }


    public void returnBookNow() {
        this.status = BorrowStatus.RETURNED;
        this.returnDate = LocalDateTime.now();
    }
}
