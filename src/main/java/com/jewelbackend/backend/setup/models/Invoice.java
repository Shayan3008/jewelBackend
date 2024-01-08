package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;
import java.util.Date;

import com.jewelbackend.backend.common.constants.Constants;

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

    @jakarta.persistence.ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "id")
    Item item;
}
