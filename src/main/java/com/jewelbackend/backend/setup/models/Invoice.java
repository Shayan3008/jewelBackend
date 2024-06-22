package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "INVOICE", schema = Constants.SETUPSCHEMA)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    Integer qty;

    @Column(name = "WASTE_PER")
    Integer wastePer;

    @Column(name = "TOTAL_ITEM_PRICE")
    BigDecimal totalItemPrice;

    @Column(name = "TOTAL_WEIGHT")
    BigDecimal totalWeight;

    @Column(name = "TOTAL_BILL")
    BigDecimal totalBill;

    @Column(name = "INVOICE_DATE")
    Date invoiceDate;

    BigDecimal making;

    @Column(name = "BEAD_AMOUNT")
    private BigDecimal beadAmount;

    @Column(name = "BIG_STONE_AMOUNT")
    private BigDecimal bigStoneAmount;

    @Column(name = "SMALL_STONE_AMOUNT")
    private BigDecimal smallStoneAmount;

    @Column(name = "DOLI_POLISH")
    private BigDecimal doliPolish;

    @Column(name = "DIAMOND_AMOUNT")
    private BigDecimal diamondAmount;

    @Column(name = "CHANDI_AMOUNT")
    private BigDecimal chandiAmount;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "DIAMOND_RATE")
    private BigDecimal diamondRate;

    @Column(name = "GOLD_RATE")
    private BigDecimal goldRate;

    @Column(name = "SOLD_ITEM_QUANTITY")
    private BigInteger soldItemQuantity;

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "id")
    Item item;
}
