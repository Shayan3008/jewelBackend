package com.jewelbackend.backend.sale.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.jewelbackend.backend.sale.model.CurrencyTransaction;

public interface CurrencyTransactionDao extends CrudRepository<CurrencyTransaction,Integer>,PagingAndSortingRepository<CurrencyTransaction,Integer> {
    
}
