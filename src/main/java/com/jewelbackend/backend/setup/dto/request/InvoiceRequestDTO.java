package com.jewelbackend.backend.setup.dto.request;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class InvoiceRequestDTO {
    Integer id;

    Integer qty;

    Integer wastePer;

    BigDecimal totalItemPrice;

    BigDecimal totalWeight;

    BigDecimal totalBill;

    Date invoiceDate;

    BigDecimal making;

    Integer itemId;
}
