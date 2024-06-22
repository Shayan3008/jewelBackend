package com.jewelbackend.backend.setup.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LedgerTransactionDto {
    BigDecimal credit;
    BigDecimal debit;
    Integer vendorId;
    String trnType;
    BigDecimal goldRate;
    BigDecimal goldWeight;
    Date date;
    BigDecimal amount;
}
