package com.jewelbackend.backend.setup.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.setup.dto.request.LoginReqDto;
import com.jewelbackend.backend.setup.dto.request.UserRequestDTO;
import com.jewelbackend.backend.setup.dto.response.LoginResponseDTO;
import com.jewelbackend.backend.setup.models.Users;
import com.jewelbackend.backend.setup.services.UserService;

@RestController
@RequestMapping("/auth")
public class LoginController {


    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponseDTO>> loginUser(@RequestBody LoginReqDto loginReqDto) {

        Users users = new Users();
        users.setEmail(loginReqDto.getEmail());
        users.setHashedPassword(loginReqDto.getPassword());
        users.setName(loginReqDto.getUserName());
        LoginResponseDTO loginResponseDTO = userService.generateTokenForUsers(users);
        return ResponseEntity.status(200)
                .body(new CommonResponse<>("User saved", HttpStatus.OK.value(), loginResponseDTO));

    }

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<LoginResponseDTO>> saveUser(@RequestBody UserRequestDTO userRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        LoginResponseDTO userResponseDTO = userService.saveUser(userRequestDTO);
        CommonResponse<LoginResponseDTO> commonResponse = new CommonResponse<>("User Registered Successfully",
                HttpStatus.OK.value(), userResponseDTO);
        return ResponseEntity.status(200).body(commonResponse);

    }
}
