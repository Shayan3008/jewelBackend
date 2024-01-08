package com.jewelbackend.backend.setup.models;

import java.util.Date;
import java.util.List;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "METAL_TYPE", schema = Constants.SETUPSCHEMA)
public class MetalType {
    @Id
    @Column(name = "METAL_NAME")
    String metalName;

    @Column(name = "PRICE_PER_GRAM")
    double pricePerGram;

    @Column(name = "ACTIVE_FLAG")
    Boolean activeFlag;

    @Column(name = "CREATED_DATE")
    Date createdDate;

    @Column(name = "CREATED_NAME")
    String createdName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "metalType",cascade = CascadeType.ALL)
    List<Category> categories;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "metalType", cascade = CascadeType.ALL)
    List<Item> items;
}
