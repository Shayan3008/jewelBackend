package com.jewelbackend.backend.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.setup.dao.UsersDAO;
import com.jewelbackend.backend.setup.models.Users;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersDAO usersDao;

    public CustomUserDetailsService(UsersDAO userRepository) {
        this.usersDao = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersDao.findByEmail(email);
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getName())
                .password(user.getHashedPassword())
                .roles(roles.toArray(new String[0]))
                .build();
    }
}
