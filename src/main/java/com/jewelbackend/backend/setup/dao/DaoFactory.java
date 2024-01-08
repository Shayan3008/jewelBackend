package com.jewelbackend.backend.setup.dao;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class DaoFactory {

    final UsersDAO usersDAO;
    final CategoryDao categoryDao;
    final SubCategoryDao subCategoryDao;
    final MetalTypeDao metalTypeDao;
    final KarigarDao karigarDao;
    final ItemDao itemDao;
    final InvoiceDao invoiceDao;
    final GoldRateDao goldRateDao;

    public DaoFactory(UsersDAO usersDAO, CategoryDao categoryDao, SubCategoryDao subCategoryDao,
            MetalTypeDao metalTypeDao, KarigarDao karigarDao, ItemDao itemDao, InvoiceDao invoiceDao,
            GoldRateDao goldRateDao) {
        this.usersDAO = usersDAO;
        this.categoryDao = categoryDao;
        this.subCategoryDao = subCategoryDao;
        this.metalTypeDao = metalTypeDao;
        this.karigarDao = karigarDao;
        this.itemDao = itemDao;
        this.invoiceDao = invoiceDao;
        this.goldRateDao = goldRateDao;
    }

}
