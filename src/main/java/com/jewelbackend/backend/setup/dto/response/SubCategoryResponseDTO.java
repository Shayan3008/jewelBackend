package com.jewelbackend.backend.setup.dto.response;

import lombok.Data;

@Data
public class SubCategoryResponseDTO {
    Integer code;

    String name;

    Boolean activeFlag;

    String categoryName;

    String metalName;
}
