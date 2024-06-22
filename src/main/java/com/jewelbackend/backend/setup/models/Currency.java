package com.jewelbackend.backend.setup.models;

import java.math.BigDecimal;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "currency",schema = Constants.SETUPSCHEMA)
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String currencyName;
    private String description;
    private BigDecimal presentAmount;
}
