package com.jewelbackend.backend.setup.services;

import org.springframework.security.authentication.AuthenticationManager;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.validator.ValidatorFactory;
import com.jewelbackend.backend.setup.dao.DaoFactory;
import com.jewelbackend.backend.setup.mapper.MapperFactory;

import lombok.Getter;

@Getter
public class BaseService {

    DaoFactory daoFactory;

    ValidatorFactory validatorFactory;

    MapperFactory mapperFactory;

    AuthenticationManager authenticationManager;

    JwtAuthConfig jwtAuthConfig;

    public BaseService(DaoFactory daoFactory, ValidatorFactory validatorFactory,
            MapperFactory mapperFactory,
            AuthenticationManager authenticationManager,
            JwtAuthConfig jwtAuthConfig) {
                this.daoFactory = daoFactory;
                this.validatorFactory = validatorFactory;
                this.mapperFactory = mapperFactory;
                this.authenticationManager = authenticationManager;
                this.jwtAuthConfig = jwtAuthConfig;
    }
}