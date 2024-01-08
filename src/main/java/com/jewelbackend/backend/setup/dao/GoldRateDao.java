package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.GoldRate;
import java.util.List;

@Repository
public interface GoldRateDao extends CrudRepository<GoldRate, Integer> {
    List<GoldRate> findByDatedString(String datedString);
}
