
package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;
import java.util.Date;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "",schema = Constants.SETUPSCHEMA)
@Data
public class GoldPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date purchaseDate;
    private String goldPurity;
    private BigDecimal goldWeight;
    private BigDecimal goldRate;
    private BigDecimal amount;
    private String description;
    private String trnType;
    private String option1;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_ID",referencedColumnName = "id")
    private Vendor vendor;
}
