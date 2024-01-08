package com.jewelbackend.backend.setup.dto.request;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MetalTypeRequestDTO {
    String metalName;

    double pricePerGram;

    Boolean activeFlag;

    Date createdDate;

    String createdName;

    List<CategoryRequestDTO> categoryRequestDTO;
    List<ItemRequestDTO> itemrRequestDTOs;
}
