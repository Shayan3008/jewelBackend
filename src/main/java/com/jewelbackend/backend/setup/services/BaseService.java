package com.jewelbackend.backend.setup.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;

import jakarta.persistence.EntityManager;
import lombok.Getter;

@Getter
public class BaseService {

    DaoFactory daoFactory;

    ValidatorFactory validatorFactory;

    MapperFactory mapperFactory;

    AuthenticationManager authenticationManager;

    JwtAuthConfig jwtAuthConfig;

    @Autowired
    EntityManager entityManager;

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