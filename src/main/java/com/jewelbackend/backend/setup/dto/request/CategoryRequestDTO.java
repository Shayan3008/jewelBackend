package com.jewelbackend.backend.setup.dto.request;

import java.util.List;

import com.jewelbackend.backend.setup.models.SubCategory;

import lombok.Data;

@Data
public class CategoryRequestDTO {

    Integer categoryCode;

    String categoryName;

    List<SubCategory> subCategory;

    String metalName;
    List<ItemRequestDTO> itemRequestDTOs;
}
