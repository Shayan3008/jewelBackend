package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

@Data
public class CurrencyRequestDTO {
    private int id;
    private String currencyName;
    private String description;
}
