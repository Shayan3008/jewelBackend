package com.jewelbackend.backend.setup.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class GoldPurchaseResponseDto {
    private Integer id;
    private Date purchaseDate;
    private String goldPurity;
    private BigDecimal goldWeight;
    private BigDecimal goldRate;
    private BigDecimal amount;
    private String description;
}
