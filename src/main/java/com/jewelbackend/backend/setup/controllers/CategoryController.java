package com.jewelbackend.backend.setup.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.CategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.services.CategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/category")
public class CategoryController {
    final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<CategoryResponseDTO>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CategoryResponseDTO> categoryResponseDTOs = categoryService.getAllCategory(page, size);
        CommonResponse<List<CategoryResponseDTO>> categoryResponseDtos = new CommonResponse<>("List All Categories",
                HttpStatus.OK.value(), categoryResponseDTOs,
                this.categoryService.getDaoFactory().getCategoryDao().count());
        return ResponseEntity.status(200).body(categoryResponseDtos);
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<CategoryResponseDTO>> saveCategory(
            @RequestBody CategoryRequestDTO categoryRequestDTO)
            throws NotPresentException, InvalidInputException, AlreadyPresentException {
        CategoryResponseDTO categoryResponseDto = this.categoryService.saveCategory(categoryRequestDTO);
        CommonResponse<CategoryResponseDTO> commonResponse = new CommonResponse<>("Category Saved",
                HttpStatus.OK.value(), categoryResponseDto);
        return ResponseEntity.status(200).body(commonResponse);

    }

    @PutMapping("/edit")
    ResponseEntity<CommonResponse<CategoryResponseDTO>> editCategory(@RequestBody CategoryRequestDTO categoryRequestDTO)
            throws InvalidInputException, AlreadyPresentException {

        CategoryResponseDTO categoryResponseDTO = this.categoryService.editCategory(categoryRequestDTO);
        CommonResponse<CategoryResponseDTO> commonResponse = new CommonResponse<>("Category edited",
                HttpStatus.OK.value(), categoryResponseDTO);
        return ResponseEntity.status(200).body(commonResponse);

    }

    @DeleteMapping("/delete/{id}")
    Integer deleteCategory(@PathVariable("id") String id) {
        this.categoryService.deleteCategory(id);
        return Integer.parseInt(id);
    }
}
