package com.jewelbackend.backend.setup.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Category;

@Repository
public interface CategoryDao extends CrudRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {
    @Query("Select cat from Category cat WHERE cat.categoryName = :categoryName AND cat.metalType.metalName = :metalName")
    List<Category> findByCategoryNameAndMetalName(String categoryName, String metalName);

    @Query("SELECT cat FROM Category cat WHERE UPPER(cat.categoryName) LIKE UPPER(:name) OR UPPER(cat.metalType.metalName) LIKE UPPER(:name)")
    Page<Category> findByCategoryNameOrMetalName(String name, PageRequest page);
}
