package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.CategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.models.Category;

@Component
public class CategoryMapper {
    public CategoryResponseDTO domainToResponse(Category category) {
        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();
        categoryResponseDTO.setActiveFlag(category.getActiveFlag());
        categoryResponseDTO.setCategoryCode(category.getCategoryCode());
        categoryResponseDTO.setCategoryName(category.getCategoryName());
        categoryResponseDTO.setMetalName(category.getMetalType().getMetalName());
        return categoryResponseDTO;
    }

    public Category requestToDomain(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setCategoryCode(categoryRequestDTO.getCategoryCode());
        category.setActiveFlag(Boolean.TRUE);
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        return category;
    }
}
