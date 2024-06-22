package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;
import java.util.Date;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "CASH_BOOK", schema = Constants.SETUPSCHEMA)
public class CashBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    BigDecimal amount;

    @Column(name = "TRN_TYPE", nullable = false)
    String trnType;

    @Column(name = "TRN_DATE", nullable = false)
    Date trnDate;

    @Column(name = "FINAL_BALANCE", nullable = false)
    BigDecimal finalBalance;

    @Column(name = "OPENING_BALANCE", nullable = false)
    BigDecimal openingBalance;
}
