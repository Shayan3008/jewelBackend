package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.GoldPurchase;

@Repository
public interface GoldPurchaseDao
        extends CrudRepository<GoldPurchase, Integer>, PagingAndSortingRepository<GoldPurchase, Integer> {

}
