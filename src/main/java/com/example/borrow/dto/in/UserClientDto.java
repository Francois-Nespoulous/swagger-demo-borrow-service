package com.example.borrow.dto.in;

import com.example.borrow.domain.enums.UserRole;

import java.util.UUID;

public record UserClientDto(UUID id, String username, UserRole userRole) {}
