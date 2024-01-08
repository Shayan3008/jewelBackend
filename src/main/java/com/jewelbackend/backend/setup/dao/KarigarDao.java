package com.jewelbackend.backend.setup.dao;

import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Karigar;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

@Repository
public interface KarigarDao extends CrudRepository<Karigar, Integer>, PagingAndSortingRepository<Karigar, Integer> {
    List<Karigar> findByKarigarName(String karigarName);
}
