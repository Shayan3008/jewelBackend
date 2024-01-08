package com.jewelbackend.backend.setup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.validator.ValidatorFactory;
import com.jewelbackend.backend.setup.dao.DaoFactory;
import com.jewelbackend.backend.setup.dto.request.SubCategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.SubCategoryResponseDTO;
import com.jewelbackend.backend.setup.mapper.MapperFactory;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.SubCategory;

@Service
public class SubCategoryService extends BaseService {
    public SubCategoryService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<SubCategoryResponseDTO> getAllSubCategory() {
        List<SubCategory> subCategories = (List<SubCategory>) getDaoFactory().getSubCategoryDao().findAll();
        return subCategories.stream().map(e -> {
            Optional<Category> category = getDaoFactory().getCategoryDao().findById(e.getParentCategory());
            if (!category.isPresent()) {

                try {
                    throw new InvalidInputException("No category exist for this");
                } catch (InvalidInputException e1) {
                    e1.printStackTrace();
                    return null;
                }

            }
            return getMapperFactory().getSubCategoryMapper().domainToResponse(e, category.get());
        }).toList();
    }

    public SubCategoryResponseDTO saveSubCategory(SubCategoryRequestDTO subCategoryRequestDTO)
            throws InvalidInputException {
        SubCategory subCategory = getMapperFactory().getSubCategoryMapper().requestToDomain(subCategoryRequestDTO);
        Optional<Category> category = getDaoFactory().getCategoryDao().findById(subCategory.getParentCategory());
        if (!category.isPresent()) {

            throw new InvalidInputException("No category exist for this");

        }
        subCategory = getDaoFactory().getSubCategoryDao().save(subCategory);
        return getMapperFactory().getSubCategoryMapper().domainToResponse(subCategory,
                category.get());
    }

    public void deleteSubCategory(String id) {
        Optional<SubCategory> category = getDaoFactory().getSubCategoryDao().findById(Integer.parseInt(id));
        if (category.isPresent()) {
            getDaoFactory().getSubCategoryDao().delete(category.get());
        }
    }

    public SubCategoryResponseDTO editSubCategory(SubCategoryRequestDTO categoryRequestDTO)
            throws InvalidInputException {
        SubCategory subCategory = getMapperFactory().getSubCategoryMapper().requestToDomain(categoryRequestDTO);
        Optional<Category> category = getDaoFactory().getCategoryDao()
                .findById(categoryRequestDTO.getParentCategory());
        if (!category.isPresent()) {
            throw new InvalidInputException("No valid metal type");
        }

        return getMapperFactory().getSubCategoryMapper().domainToResponse(subCategory,
                category.get());
    }
}
