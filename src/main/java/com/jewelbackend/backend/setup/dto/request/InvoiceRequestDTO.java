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

    BigDecimal itemWeight;

    BigDecimal totalBill;

    Date invoiceDate;

    BigDecimal making;

    Integer itemId;

    BigDecimal beadAmount;

    BigDecimal bigStoneAmount;

    BigDecimal smallStoneAmount;

    BigDecimal doliPolish;

    BigDecimal diamondAmount;

    BigDecimal chandiAmount;

    BigDecimal discount;

    String description;

    BigDecimal diamondRate;

    BigDecimal goldRate;
}
