package com.jewelbackend.backend.setup.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CashBookRequestDTO {
    int id;
    BigDecimal amount;

    String trnType;

    Date trnDate;
}
