package com.jewelbackend.backend.setup.models;

import java.util.List;

import com.jewelbackend.backend.common.constants.Constants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "KARIGAR", schema = Constants.SETUPSCHEMA)
public class Karigar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "KARIGAR_NAME")
    String karigarName;
    
    @Column(name = "description")
    String description;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "karigar")
    List<Item> items;
}
