package com.jewelbackend.backend.setup.dto.response;

import lombok.Data;

@Data
public class ItemResponseDTO {
    int id;

    String itemName;

    Integer karat;
    String designNo;

    Double netWeight;
    String itemImage;

    int karigarId;
    String karigarName;
    String metalName;

    int categoryId;
    String categoryName;
}
