package com.jewelbackend.backend.setup.dto.response;

import java.util.List;



import lombok.Data;

@Data
public class CategoryResponseDTO {
    Integer categoryCode;

    String categoryName;

    Boolean activeFlag;

    List<ItemResponseDTO> itemResponseDTOs;

    String metalName;
}
