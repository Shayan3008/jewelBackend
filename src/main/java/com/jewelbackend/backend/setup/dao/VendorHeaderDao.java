package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.VendorHeader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorHeaderDao extends PagingAndSortingRepository<VendorHeader,Integer>, CrudRepository<VendorHeader,Integer> {
    VendorHeader findByName(String name);
}
