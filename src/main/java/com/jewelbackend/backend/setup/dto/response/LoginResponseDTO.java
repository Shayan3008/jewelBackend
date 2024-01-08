package com.jewelbackend.backend.setup.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String userName;
    private String email;
    private String token;
}