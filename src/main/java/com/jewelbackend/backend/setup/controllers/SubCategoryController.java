package com.jewelbackend.backend.setup.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.setup.dto.request.SubCategoryRequestDTO;
import com.jewelbackend.backend.setup.dto.response.SubCategoryResponseDTO;
import com.jewelbackend.backend.setup.services.SubCategoryService;

@RestController
@RequestMapping("/subcategory")
public class SubCategoryController {
    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("")
    public List<SubCategoryResponseDTO> getAllSubCategory() {
        try {

            return this.subCategoryService.getAllSubCategory();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    @PostMapping("/save")
    public SubCategoryResponseDTO saveSubCategory(@RequestBody SubCategoryRequestDTO subCategoryRequestDTO) {
        try {

            return this.subCategoryService.saveSubCategory(subCategoryRequestDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    Integer deleteSubCategory(@PathVariable("id") String id) {
        this.subCategoryService.deleteSubCategory(id);
        return Integer.parseInt(id);
    }

    @PutMapping("/edit")
    public SubCategoryResponseDTO editSubCategory(SubCategoryRequestDTO subCategoryRequestDTO) {
        try {

            return this.subCategoryService.editSubCategory(subCategoryRequestDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
