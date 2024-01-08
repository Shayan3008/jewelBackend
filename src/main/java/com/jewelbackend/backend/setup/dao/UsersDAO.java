package com.jewelbackend.backend.setup.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Users;


@Repository
public interface UsersDAO extends CrudRepository<Users, Integer>{
    Users findByEmail(String email);
}
