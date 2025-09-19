package com.example.borrow.domain.service;

import com.example.borrow.exception.specific.InvalidCredentialsException;
import com.example.borrow.persistence.repository.entity.UserEntity;
import com.example.borrow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthentificationService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthentificationService(UserRepository userRepository,
                                   BCryptPasswordEncoder passwordEncoder,
                                   JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String login(String username, String password) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException(username));

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new InvalidCredentialsException(username);
        }

        return jwtService.generateToken(userEntity.getUsername());
    }
}
