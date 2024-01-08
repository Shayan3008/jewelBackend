package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.MetalType;

@Repository
public interface MetalTypeDao extends CrudRepository<MetalType, String> {
}
