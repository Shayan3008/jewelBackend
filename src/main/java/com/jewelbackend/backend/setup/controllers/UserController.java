package com.jewelbackend.backend.setup.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.setup.dto.response.UserResponseDTO;
import com.jewelbackend.backend.setup.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    
    UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    
}
