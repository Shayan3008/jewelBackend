package com.jewelbackend.backend.setup.dao;

import com.jewelbackend.backend.setup.models.CashBookView;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashBookViewDao extends CrudRepository<CashBookView, Integer> {
}
