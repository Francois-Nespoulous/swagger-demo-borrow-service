package com.example.borrow.config;

import com.example.borrow.client.UserClient;
import com.example.borrow.dto.in.UserClientDto;
import com.example.borrow.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserClient userClient;

    @Autowired
    public UserDetailsServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserClientDto userClientDto = userClient.getUserByUsername(username);

        return new UserDetailsImpl(UserMapper.toDomain(userClientDto, 0));
    }
}
