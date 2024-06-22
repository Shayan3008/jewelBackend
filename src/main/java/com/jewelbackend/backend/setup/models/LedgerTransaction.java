package com.jewelbackend.backend.setup.models;

import com.jewelbackend.backend.common.constants.Constants;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "LEDGER_TRANSACTION",schema = Constants.SETUPSCHEMA)
@Data
public class LedgerTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    BigDecimal credit;
    BigDecimal debit;
//    @Column(name = "TRN_DATE")
    Date trnDate;
    @Column(name = "CREDIT_GOLD_WEIGHT")
    BigDecimal creditGoldWeight;

    @Column(name = "DEBIT_GOLD_WEIGHT")
    BigDecimal debitGoldWeight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendorId", referencedColumnName = "id")
    Vendor vendor;
}
