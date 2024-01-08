package com.jewelbackend.backend.setup.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Category;

@Repository
public interface CategoryDao extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {
    @Query("Select cat from Category cat WHERE cat.categoryName = :categoryName AND cat.metalType.metalName = :metalName")
    List<Category> findByCategoryNameAndMetalName(String categoryName, String metalName);
}
