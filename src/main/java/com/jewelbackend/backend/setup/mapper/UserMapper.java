package com.jewelbackend.backend.setup.mapper;
import org.springframework.stereotype.Component;
import com.jewelbackend.backend.setup.dto.request.UserRequestDTO;
import com.jewelbackend.backend.setup.dto.response.UserResponseDTO;
import com.jewelbackend.backend.setup.models.Users;

@Component
public class UserMapper {
 
    public Users requestToDomain(UserRequestDTO userRequestDTO){
        Users users = new Users();
        users.setEmail(userRequestDTO.getEmail());
        users.setName(userRequestDTO.getName());
        users.setHashedPassword(userRequestDTO.getHashedPassword());
        return users;
    }

    public UserResponseDTO domainToResponse(Users user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        return userResponseDTO;
    }
}
