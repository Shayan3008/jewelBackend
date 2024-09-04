package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.MetalType;

@Repository
public interface MetalTypeDao extends CrudRepository<MetalType, String>, PagingAndSortingRepository<MetalType, String> {
}
