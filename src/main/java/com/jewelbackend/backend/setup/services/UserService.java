package com.jewelbackend.backend.setup.services;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.UserRequestDTO;
import com.jewelbackend.backend.setup.dto.response.LoginResponseDTO;
import com.jewelbackend.backend.setup.dto.response.UserResponseDTO;
import com.jewelbackend.backend.setup.models.Users;

@Service
public class UserService extends BaseService {
   
    private PasswordEncoder passwordEncoder;
    
    public UserService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig,PasswordEncoder passwordEncoder) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDTO> getAllUsers() {

        List<Users> users = (List<Users>) getDaoFactory().getUsersDAO().findAll();
        return users.stream().map(e -> getMapperFactory().getUserMapper().domainToResponse(e)).toList();
    }

    public LoginResponseDTO saveUser(UserRequestDTO userRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        String hashPassword = passwordEncoder.encode(userRequestDTO.getHashedPassword());
        Users users = getMapperFactory().getUserMapper().requestToDomain(userRequestDTO);
        users.setHashedPassword(hashPassword);
        String validation = getValidatorFactory().getUserValidator().validateUser(users);
        if (validation != null) {
            throw new InvalidInputException(validation);
        }
        if (getDaoFactory().getUsersDAO().findByEmail(userRequestDTO.getEmail()) != null) {
            throw new AlreadyPresentException("Email is already Present");
        }
        users = getDaoFactory().getUsersDAO().save(users);
        users.setHashedPassword(userRequestDTO.getHashedPassword());
        return generateTokenForUsers(users);

    }

    public LoginResponseDTO generateTokenForUsers(Users users) {
        Authentication auth = getAuthenticationManager()
                .authenticate(
                        new UsernamePasswordAuthenticationToken(users.getEmail(), users.getHashedPassword()));
        String token = jwtAuthConfig.createToken(users);
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        loginResponseDTO.setEmail(users.getEmail());
        loginResponseDTO.setUserName(auth.getName());
        loginResponseDTO.setToken(token);
        return loginResponseDTO;
    }

}
