package com.jewelbackend.backend.setup.dto.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CurrencyResponseDTO {
    private int id;
    private String currencyName;
    private String description; 
    private BigDecimal presentAmount;  
}
