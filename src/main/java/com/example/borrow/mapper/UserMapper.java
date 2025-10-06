package com.example.borrow.mapper;

import com.example.borrow.domain.model.User;
import com.example.borrow.dto.in.UserClientDto;
import com.example.borrow.dto.out.UserDto;

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
