package com.jewelbackend.backend.setup.dto.request;


import lombok.Data;

@Data
public class ItemRequestDTO {
    int id;

    String itemName;

    Integer karat;

    String designNo;

    Double netWeight;

    String itemImage;

    int karigarId;

    String metalName;

    int categoryId;
}
