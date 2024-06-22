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

    String categoryName;

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

    ItemResponseDTO itemResponseDTO;
}
