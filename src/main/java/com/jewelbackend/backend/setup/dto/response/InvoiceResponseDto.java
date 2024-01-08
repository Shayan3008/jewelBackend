package com.jewelbackend.backend.setup.dto.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class InvoiceResponseDto {
    Integer id;

    Integer qty;

    Integer wastePer;

    BigDecimal totalItemPrice;

    BigDecimal totalWeight;

    BigDecimal totalBill;

    Date invoiceDate;

    BigDecimal making;

    String itemName;
}
