package com.jewelbackend.backend.setup.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.common.validator.ValidatorFactory;
import com.jewelbackend.backend.setup.dao.DaoFactory;
import com.jewelbackend.backend.setup.dto.request.CategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.mapper.MapperFactory;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.MetalType;

@Service
public class CategoryService extends BaseService {
    public CategoryService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<CategoryResponseDTO> getAllCategory(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Category> categoryPage = getDaoFactory().getCategoryDao().findAll(pageRequest);
        List<Category> categories = categoryPage.getContent();
        return categories.stream().map(e -> getMapperFactory().getCategoryMapper().domainToResponse(e)).toList();
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO)
            throws NotPresentException, InvalidInputException, AlreadyPresentException {
        Optional<MetalType> metalType = getDaoFactory().getMetalTypeDao().findById(categoryRequestDTO.getMetalName());
        if (!metalType.isPresent()) {
            throw new NotPresentException("Metal name is not present");
        }
        Category category = getMapperFactory().getCategoryMapper().requestToDomain(categoryRequestDTO);
        category.setMetalType(metalType.get());
        String validate = getValidatorFactory().getCategoryValidator().validateCategory(category);
        if (validate != null)
            throw new InvalidInputException(validate);
        List<Category> categories = getDaoFactory().getCategoryDao()
                .findByCategoryNameAndMetalName(category.getCategoryName(), categoryRequestDTO.getMetalName());
        if (!categories.isEmpty()) {
            throw new AlreadyPresentException("Category with name already found");
        }
        category = getDaoFactory().getCategoryDao().save(category);
        return getMapperFactory().getCategoryMapper().domainToResponse(category);
    }

    public void deleteCategory(String id) {
        Optional<Category> category = getDaoFactory().getCategoryDao().findById(Integer.parseInt(id));
        if (category.isPresent()) {
            getDaoFactory().getCategoryDao().delete(category.get());
        }
    }

    public CategoryResponseDTO editCategory(CategoryRequestDTO categoryRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        Optional<MetalType> metalType = getDaoFactory().getMetalTypeDao()
                .findById(categoryRequestDTO.getMetalName());
        if (!metalType.isPresent()) {
            throw new InvalidInputException("No valid metal type");
        }
        Category category = getMapperFactory().getCategoryMapper().requestToDomain(categoryRequestDTO);
        category.setMetalType(metalType.get());
        List<Category> categories = getDaoFactory().getCategoryDao()
                .findByCategoryNameAndMetalName(category.getCategoryName(), categoryRequestDTO.getMetalName());
        if (!categories.isEmpty()) {
            throw new AlreadyPresentException("Category with name already found");
        }
        category = getDaoFactory().getCategoryDao().save(category);

        return getMapperFactory().getCategoryMapper().domainToResponse(category);
    }
}
