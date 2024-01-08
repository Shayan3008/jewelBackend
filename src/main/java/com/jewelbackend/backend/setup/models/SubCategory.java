package com.jewelbackend.backend.setup.models;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "SUB_CATEGORY", schema = Constants.SETUPSCHEMA)
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer code;

    String name;

    @Column(name = "ACTIVE_FLAG")
    Boolean activeFlag;

    @Column(name = "PARENT_CATEGORY")
    Integer parentCategory;
}
