package com.jewelbackend.backend.setup.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Invoice;

@Repository
public interface InvoiceDao extends CrudRepository<Invoice, Integer>, PagingAndSortingRepository<Invoice, Integer> {

}
