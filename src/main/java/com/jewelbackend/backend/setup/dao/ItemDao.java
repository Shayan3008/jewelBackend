package com.jewelbackend.backend.setup.dao;

import org.springframework.stereotype.Repository;

import com.jewelbackend.backend.setup.models.Item;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@Repository
public interface ItemDao extends CrudRepository<Item, Integer>, PagingAndSortingRepository<Item, Integer> {
    List<Item> findByItemName(String itemName);
}
