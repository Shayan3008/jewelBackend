package com.jewelbackend.backend.setup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jewelbackend.backend.common.constants.Constants;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "vendor", schema = Constants.SETUPSCHEMA)
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENDOR_HEADER_ID", referencedColumnName = "ID")
    VendorHeader vendorHeader;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    List<LedgerTransaction> ledgerTransactions;
}
