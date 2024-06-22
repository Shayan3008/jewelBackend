package com.jewelbackend.backend.setup.dto.request;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Data;

@Data
public class ItemRequestDTO {
    int id;

    String karat;

    String designNo;

    BigDecimal netWeight;

    BigDecimal itemWeight;

    String itemImage;

    int karigarId;

    String metalName;

    int categoryId;

    String description;

    BigInteger qty;

    BigDecimal beedsWeight;

    BigDecimal bigStoneWeight;

    BigInteger smallStoneQty;

    BigInteger diamondQty;

    BigDecimal diamondWeight;



}
