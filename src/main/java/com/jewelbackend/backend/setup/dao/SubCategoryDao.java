package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.SubCategory;

@Repository
public interface SubCategoryDao extends CrudRepository<SubCategory, Integer> {
}