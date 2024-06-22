package com.jewelbackend.backend.setup.dao;

import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.CashBook;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Date;
import java.util.List;

@Repository
public interface CashBookDao extends CrudRepository<CashBook, Integer>, PagingAndSortingRepository<CashBook, Integer> {

    @Query("Select e from CashBook e WHERE UPPER(e.trnType) = UPPER(:type)")
    Page<CashBook> findAllByTrnType(String type, PageRequest pageable);

    @Query("Select e from CashBook e Where e.trnDate = :trnDate")
    Page<CashBook> findAllByTrnDate(Date trnDate, PageRequest pageRequest);

    @Query("Select e from CashBook e Where UPPER(e.trnType) = UPPER(:type) And e.trnDate = :trnDate")
    Page<CashBook> findAllByTrnDateAndTrnType(String type,Date trnDate, PageRequest pageRequest);

    @Query("SELECT e from CashBook e where e.trnDate <= :trnDate order by e.id desc")
    List<CashBook> findLastTransaction(Date trnDate);

}
