package com.jewelbackend.backend.setup.models;

import com.jewelbackend.backend.common.constants.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "VENDOR_HEADER", schema = Constants.SETUPSCHEMA)
@Getter
@Setter
public class VendorHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;

    String description;

    @OneToMany(mappedBy = "vendorHeader", fetch = FetchType.LAZY)
    List<Vendor> vendors;
}
