package com.jewelbackend.backend.setup.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class KarigarRequestDTO {
    int id;
    String karigarName;
    String description;
    List<ItemRequestDTO> itemrRequestDTOs;
}
