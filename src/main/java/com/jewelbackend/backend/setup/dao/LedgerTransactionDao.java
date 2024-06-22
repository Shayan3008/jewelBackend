package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.LedgerTransaction;
import com.jewelbackend.backend.setup.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LedgerTransactionDao extends CrudRepository<LedgerTransaction,Integer>, PagingAndSortingRepository<LedgerTransaction,Integer>, JpaRepository<LedgerTransaction,Integer>,
        JpaSpecificationExecutor<LedgerTransaction>{
    List<LedgerTransaction> findByVendor(Vendor vendor);

    @Query(value = "select e from LedgerTransaction e where e.creditGoldWeight is not null order by e.id desc limit 1")
    LedgerTransaction getCreditGoldWeight();
    @Query(value = "select e from LedgerTransaction e where e.debitGoldWeight is not null order by e.id desc limit 1")
    LedgerTransaction getDebitGoldWeight();

    @Query(value = "select e from LedgerTransaction e where e.creditGoldWeight is not null AND e.vendor = :vendor order by e.id desc limit 1")
    LedgerTransaction getCreditGoldWeightByVendor(Vendor vendor);

    @Query(value = "select e from LedgerTransaction e where e.debitGoldWeight is not null AND e.vendor = :vendor order by e.id desc limit 1")
    LedgerTransaction getDebitGoldWeightByVendor(Vendor vendor);

}
