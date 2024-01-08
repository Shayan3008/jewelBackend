package com.jewelbackend.backend.setup.models;

import java.util.List;


import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CATEGORY", schema = Constants.SETUPSCHEMA)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_CODE")
    Integer categoryCode;

    @Column(name = "CATEGORY_NAME")
    String categoryName;

    @Column(name = "ACTIVE_FLAG")
    Boolean activeFlag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "METAL_NAME", referencedColumnName = "METAL_NAME")
    MetalType metalType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    List<Item> items;

}
