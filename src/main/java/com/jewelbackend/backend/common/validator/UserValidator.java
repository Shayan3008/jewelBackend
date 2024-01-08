package com.jewelbackend.backend.common.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.setup.models.Users;

@Component
public class UserValidator {
    public String validateUser(Users user) {
        if (Objects.isNull(user.getName()))
            return "Name could not be empty";
        if (Objects.isNull(user.getEmail()) || Constants.matchEmail(user.getEmail()).equals(Boolean.FALSE))
            return "Enter proper email format.";
        if (Objects.isNull(user.getHashedPassword()) || user.getHashedPassword().length() < 5)
            return "Password should be greater than 5 lengths";
        return null;
    }
}
