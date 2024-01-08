package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

@Data
public class SubCategoryRequestDTO {
    Integer code;

    String name;

    Boolean activeFlag;

    Integer parentCategory;
}
