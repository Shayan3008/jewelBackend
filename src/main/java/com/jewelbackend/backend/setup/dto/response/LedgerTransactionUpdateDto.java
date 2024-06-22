package com.jewelbackend.backend.setup.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LedgerTransactionUpdateDto {
    private BigDecimal debitGoldWeight;
    private BigDecimal creditGoldWeight;
}
