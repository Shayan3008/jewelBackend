package com.jewelbackend.backend.jobs;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MetalPriceDto {
    private String[] validationMessage;
    private Rates rates;

    @Data
    public static class Rates {
        @JsonProperty("XAU_24K")
        private BigDecimal xau24k;
    }
}
