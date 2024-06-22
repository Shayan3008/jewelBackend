package com.jewelbackend.backend.setup.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Invoice;

@Repository
public interface InvoiceDao extends CrudRepository<Invoice, Integer>, PagingAndSortingRepository<Invoice, Integer> {

    @Query("Select cat from Invoice cat where UPPER(cat.item.category.categoryName) LIKE UPPER(:name) OR UPPER(cat.item.karigar.karigarName) LIKE UPPER(:name)")
    Page<Invoice> findAllByKarigarNameOrCategoryName(String name,PageRequest pageRequest);
}
