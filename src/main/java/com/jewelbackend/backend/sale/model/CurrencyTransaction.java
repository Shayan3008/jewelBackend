package com.jewelbackend.backend.sale.model;

import java.math.BigDecimal;
import java.util.Date;

import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.setup.models.Currency;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "currency_transaction",schema = Constants.SETUPSCHEMA)
public class CurrencyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private BigDecimal exchangeRate;
    private String trnType;
    private String description;
    private BigDecimal qty;
    private Date transactionDate;
    @ManyToOne
    @JoinColumn(name = "currency_code",referencedColumnName = "id")
    private Currency currency;
}
