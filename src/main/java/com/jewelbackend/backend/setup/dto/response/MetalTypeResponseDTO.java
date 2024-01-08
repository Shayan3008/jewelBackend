package com.jewelbackend.backend.setup.dto.response;

import java.util.List;

import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;

import lombok.Data;

@Data
public class MetalTypeResponseDTO {
    String metalName;

    double pricePerGram;

    List<ItemRequestDTO> itemrRequestDTOs;

    List<CategoryResponseDTO> categoryResponseDTOs;
}
