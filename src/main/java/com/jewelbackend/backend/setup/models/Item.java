package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    String karat;

    @Column(name = "DESIGN_NO")
    String designNo;

    @Column(name = "NET_WEIGHT")
    BigDecimal netWeight;

    @Column(name = "REMAINING_NET_WEIGHT")
    BigDecimal remainingNetWeight;

    @Column(name = "ITEM_IMAGE")
    @Lob
    byte[] itemImage;

    String description;

    BigInteger qty;

    @Column(name = "BEEDS_WEIGHT")
    BigDecimal beedsWeight;

    @Column(name = "BIG_STONE_WEIGHT")
    BigDecimal bigStoneWeight;

    @Column(name = "SMALL_STONE_QTY")
    BigInteger smallStoneQty;

    @Column(name = "DIAMOND_QTY")
    BigInteger diamondQty;

    @Column(name = "DIAMOND_WEIGHT")
    BigDecimal diamondWeight;

    @Column(name = "MULTI_ITEM")
    Boolean multiItem;

    @Column(name = "TOTAL_QTY")
    BigInteger totalQty;

    @Column(name = "TOTAL_MULTI_WEIGHT")
    BigDecimal totalMultiWeight;

    @Column(name = "ITEM_IMAGE_PATH")
    String itemImagePath;

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
