package com.jewelbackend.backend.setup.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CashBookResponseDTO {
    int id;
    BigDecimal amount;

    String trnType;

    Date trnDate;
}
