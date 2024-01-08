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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ITEM", schema = Constants.SETUPSCHEMA)
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "ITEM_NAME")
    String itemName;

    Integer karat;
    
    @Column(name = "DESIGN_NO")
    String designNo;

    @Column(name = "NET_WEIGHT")
    Double netWeight;

    

    @Column(name = "ITEM_IMAGE")
    @Lob
    byte[] itemImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "karigarId", referencedColumnName = "id")
    Karigar karigar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metalName", referencedColumnName = "METAL_NAME")
    MetalType metalType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category", referencedColumnName = "CATEGORY_CODE")
    Category category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
    List<Invoice> items;
}
