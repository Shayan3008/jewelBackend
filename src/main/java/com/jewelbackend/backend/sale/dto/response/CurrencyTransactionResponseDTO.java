package com.jewelbackend.backend.sale.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyTransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private String trnType;
    private String description;
    private BigDecimal qty;
    private String currencyName;
    private int currencyId;
    private BigDecimal presentAmount;
}
