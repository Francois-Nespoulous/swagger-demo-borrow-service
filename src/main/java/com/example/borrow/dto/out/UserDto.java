package com.example.borrow.dto.out;

import com.example.borrow.domain.enums.UserRole;

import java.util.UUID;

public record UserDto(UUID id, String username, int nbOfBooksBorrowed, UserRole userRole) {}
