package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.Vendor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorDao extends CrudRepository<Vendor,Integer>, PagingAndSortingRepository<Vendor,Integer> {
}
