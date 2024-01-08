package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "GOLD_RATE", schema = Constants.SETUPSCHEMA)
public class GoldRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "DATED_STRING")
    String datedString;
    BigDecimal price;
}
