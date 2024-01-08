package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.SubCategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.SubCategoryResponseDTO;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.SubCategory;

@Component
public class SubCategoryMapper {
    public SubCategory requestToDomain(SubCategoryRequestDTO e) {
        SubCategory subCategory = new SubCategory();
        subCategory.setCode(e.getCode());
        subCategory.setName(e.getName());
        subCategory.setActiveFlag(e.getActiveFlag());
        subCategory.setParentCategory(e.getParentCategory());
        return subCategory;
    }

    public SubCategoryResponseDTO domainToResponse(SubCategory subCategory, Category category) {
        SubCategoryResponseDTO subCategoryResponseDTO = new SubCategoryResponseDTO();
        subCategoryResponseDTO.setCode(subCategory.getCode());
        subCategoryResponseDTO.setName(subCategory.getName());
        subCategoryResponseDTO.setActiveFlag(subCategory.getActiveFlag());
        subCategoryResponseDTO.setCategoryName(category.getCategoryName());
        // subCategoryResponseDTO.setMetalName(category.getMetalName());
        return subCategoryResponseDTO;
    }
}
