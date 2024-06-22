package com.jewelbackend.backend.setup.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class GoldPurchaseRequestDto {
    private Integer id;
    private Date purchaseDate;
    private String goldPurity;
    private String trnType;
    private BigDecimal goldWeight;
    private BigDecimal goldRate;
    private BigDecimal amount;
    private String description;
    private Integer vendorId;
    private String option;
}
