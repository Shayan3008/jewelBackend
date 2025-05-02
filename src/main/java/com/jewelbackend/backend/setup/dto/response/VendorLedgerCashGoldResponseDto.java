package com.jewelbackend.backend.setup.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendorLedgerCashGoldResponseDto {
    private BigDecimal cash;
    private BigDecimal gold;
}
