package com.example.borrow.domain.model;

import com.example.borrow.domain.enums.UserRole;

import java.util.UUID;

public class User {
    private UUID id;
    private String username;
    private int nbOfBooksBorrowed;
    private UserRole userRole;

    public User(UUID id, String username, int nbOfBooksBorrowed, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.nbOfBooksBorrowed = nbOfBooksBorrowed;
        this.userRole = userRole;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getNbOfBooksBorrowed() {
        return nbOfBooksBorrowed;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setNbOfBooksBorrowed(int nbOfBooksBorrowed) {
        this.nbOfBooksBorrowed = nbOfBooksBorrowed;
    }
}
