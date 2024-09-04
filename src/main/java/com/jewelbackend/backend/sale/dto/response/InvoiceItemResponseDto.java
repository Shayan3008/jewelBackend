package com.jewelbackend.backend.sale.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class InvoiceItemResponseDto {
    private Integer id;

    private Integer categoryId;

    private BigDecimal netWeight;

    private String karigarName;
}
