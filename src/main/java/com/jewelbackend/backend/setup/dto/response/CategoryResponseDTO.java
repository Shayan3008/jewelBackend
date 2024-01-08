package com.jewelbackend.backend.setup.dto.response;

import java.util.List;

import com.jewelbackend.backend.setup.models.SubCategory;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    Integer categoryCode;

    String categoryName;

    Boolean activeFlag;

    List<SubCategory> subCategory;

    String metalName;
}
