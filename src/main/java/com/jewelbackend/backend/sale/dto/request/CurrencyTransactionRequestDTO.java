package com.jewelbackend.backend.sale.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyTransactionRequestDTO {
    private Long id;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private String trnType;
    private String description;
    private BigDecimal qty;
    private int currencyId;
}
