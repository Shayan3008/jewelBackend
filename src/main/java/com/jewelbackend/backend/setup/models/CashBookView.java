package com.jewelbackend.backend.setup.models;

import com.jewelbackend.backend.common.constants.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "cb_view", schema = Constants.SETUPSCHEMA)
@Getter
@Setter
public class CashBookView {
    @Id
    private Integer id;

    private Date dated;

    private BigDecimal amount;

    private String type1;
}
