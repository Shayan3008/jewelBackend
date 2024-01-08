package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String name;
    private String email;
    private String hashedPassword;
}
