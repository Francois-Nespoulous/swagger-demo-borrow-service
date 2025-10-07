package com.example.borrow.domain.mapper;

import com.example.borrow.domain.model.User;
import com.example.borrow.domain.service.ext.UserClientDto;
import com.example.borrow.controller.dto.out.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getNbOfBooksBorrowed(),
                user.getUserRole()
        );
    }

    public static User toDomain(UserClientDto userClientDto, int nbOfBooksBorrowed) {
        return new User(
                userClientDto.id(),
                userClientDto.username(),
                nbOfBooksBorrowed,
                userClientDto.userRole()
        );
    }
}
