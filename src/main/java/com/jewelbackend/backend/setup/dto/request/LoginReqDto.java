package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

@Data
public class LoginReqDto {
    private String userName;
    private String password;
    private String email;
}
