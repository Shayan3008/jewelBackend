package com.jewelbackend.backend.setup.dto.response;

import java.util.List;

import lombok.Data;

@Data
public class KarigarResponseDTO {
    int id;
    String karigarName;
    String description;
    List<ItemResponseDTO> itemRequestDTOs;
}
