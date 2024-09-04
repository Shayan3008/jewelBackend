package com.jewelbackend.backend.setup.dto.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ItemResponseDTO {
    int id;

    String karat;
    String designNo;

    BigDecimal netWeight;
    String itemImage;

    int karigarId;
    String karigarName;
    String metalName;

    int categoryId;
    String categoryName;

    String description;

    BigInteger qty;

    BigDecimal beedsWeight;

    BigDecimal bigStoneWeight;

    BigInteger smallStoneQty;

    BigInteger diamondQty;

    BigDecimal diamondWeight;

    Boolean multiItem;

    BigInteger totalQty;

    BigDecimal totalMultiWeight;

    BigDecimal remainingNetWeight;

}
