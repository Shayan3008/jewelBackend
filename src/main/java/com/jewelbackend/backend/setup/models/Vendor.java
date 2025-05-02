package com.jewelbackend.backend.setup.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jewelbackend.backend.common.constants.Constants;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
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

    @Column(name = "CUS_CODE")
    String cusCode;

    @OneToMany(mappedBy = "vendor", fetch = FetchType.LAZY)
    List<LedgerTransaction> ledgerTransactions;
}
