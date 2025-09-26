package com.example.borrow.mapper;

import com.example.borrow.domain.enums.BorrowStatus;
import com.example.borrow.domain.enums.UserRole;
import com.example.borrow.domain.model.User;
import com.example.borrow.dto.in.CreateUserRequest;
import com.example.borrow.dto.out.UserDto;
import com.example.borrow.persistence.repository.entity.UserEntity;
import com.example.borrow.repository.BorrowRepository;

public class UserMapper {
    public static User toDomain(UserEntity userEntity, BorrowRepository borrowRepository) {
        int nbOfBooksBorrowed = borrowRepository.countBorrowedByUserId_AndStatusEquals(userEntity.getId(), BorrowStatus.ONGOING);
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                nbOfBooksBorrowed,
                userEntity.getUserRole()
        );
    }

    public static User toDomainLight(UserEntity userEntity) {
        return new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                0,
                userEntity.getUserRole()
        );
    }

    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getNbOfBooksBorrowed(),
                user.getUserRole()
        );
    }

    public static User toDomain(CreateUserRequest createUserRequest) {
        return new User(
                null,
                createUserRequest.username(),
                null,
                0,
                UserRole.USER
        );
    }

    public static UserEntity toEntity(User user, String hashedPassword) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                hashedPassword,
                user.getUserRole()
        );
    }

    public static User toDomain(UserDto userDto) {
        return new User(
                userDto.id(),
                userDto.username(),
                null,
                userDto.nbOfBooksBorrowed(),
                userDto.userRole()
        );
    }
}
