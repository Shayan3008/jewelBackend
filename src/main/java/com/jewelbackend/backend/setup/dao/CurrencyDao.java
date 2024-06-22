package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Currency;

@Repository
public interface CurrencyDao extends CrudRepository<Currency, Integer>,PagingAndSortingRepository<Currency,Integer> {

}
