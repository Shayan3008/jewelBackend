package com.jewelbackend.backend.setup.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.CategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.models.Category;
import com.jewelbackend.backend.setup.models.Item;
import com.jewelbackend.backend.setup.models.MetalType;

@Service
public class CategoryService extends BaseService {
    public CategoryService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<CategoryResponseDTO> getCategoryLov(){
        List<Category> categories  = (List<Category>) getDaoFactory().getCategoryDao().findAll();;

        return categories.stream().map(e -> {
            CategoryResponseDTO categoryResponseDTO = getMapperFactory().getCategoryMapper().domainToResponse(e);
            return categoryResponseDTO;
        }).toList();
    }


    public List<CategoryResponseDTO> getAllCategory(int page, int size, String search) {
        List<Category> categories = null;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (search.isEmpty()) {
            Page<Category> categoryPage = getDaoFactory().getCategoryDao().findAll(pageRequest);
            categories = categoryPage.getContent();
        } else {
            search = "%" + search + "%";
            Page<Category> categoryPage = getDaoFactory().getCategoryDao().findByCategoryNameOrMetalName(search,
                    pageRequest);
            categories = categoryPage.getContent();
        }
        return categories.stream().map(e -> {
//            List<Item> items = e.getItems().stream()
//                    .filter(item -> item != null && item.getRemainingNetWeight() != null && !item.getRemainingNetWeight().setScale(2).equals(BigDecimal.valueOf(0).setScale(2)))
//                    .toList();
//            List<ItemResponseDTO> itemResponseDTOs = items.stream()
//                    .map(item -> getMapperFactory().getItemMapper().domainToResponse(item))
//                    .collect(Collectors.toList());
            CategoryResponseDTO categoryResponseDTO = getMapperFactory().getCategoryMapper().domainToResponse(e);
//            categoryResponseDTO.setItemResponseDTOs(itemResponseDTOs);
            return categoryResponseDTO;
        }).toList();
    }

    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryRequestDTO)
            throws NotPresentException, InvalidInputException, AlreadyPresentException {
        Optional<MetalType> metalType = getDaoFactory().getMetalTypeDao().findById(categoryRequestDTO.getMetalName());
        if (metalType.isEmpty()) {
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
        category.ifPresent(value -> getDaoFactory().getCategoryDao().delete(value));
    }

    public CategoryResponseDTO editCategory(CategoryRequestDTO categoryRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        Optional<MetalType> metalType = getDaoFactory().getMetalTypeDao()
                .findById(categoryRequestDTO.getMetalName());
        if (metalType.isEmpty()) {
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
