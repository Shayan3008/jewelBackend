package com.jewelbackend.backend.factorybeans;

import com.jewelbackend.backend.setup.dao.*;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.sale.dao.CurrencyTransactionDao;

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
    final CashBookDao cashBookDao;
    final GoldRateDao goldRateDao;
    final CurrencyTransactionDao currencyTransactionDao;
    final CurrencyDao currencyDao;
    final GoldPurchaseDao goldPurchaseDao;
    final VendorDao vendorDao;
    final LedgerTransactionDao ledgerTransactionDao;
    final VendorHeaderDao vendorHeaderDao;
    final CashBookViewDao cashBookViewDao;

    public DaoFactory(
            UsersDAO usersDAO, CategoryDao categoryDao, SubCategoryDao subCategoryDao,
            MetalTypeDao metalTypeDao, KarigarDao karigarDao, ItemDao itemDao, InvoiceDao invoiceDao,
            GoldRateDao goldRateDao, CashBookDao cashBookDao, CurrencyTransactionDao currencyTransactionDao,
            CurrencyDao currencyDao, GoldPurchaseDao goldPurchaseDao, VendorDao vendorDao,
            LedgerTransactionDao ledgerTransactionDao, VendorHeaderDao vendorHeaderDao, CashBookViewDao cashBookViewDao) {
        this.usersDAO = usersDAO;
        this.categoryDao = categoryDao;
        this.subCategoryDao = subCategoryDao;
        this.metalTypeDao = metalTypeDao;
        this.karigarDao = karigarDao;
        this.itemDao = itemDao;
        this.invoiceDao = invoiceDao;
        this.goldRateDao = goldRateDao;
        this.cashBookDao = cashBookDao;
        this.currencyTransactionDao = currencyTransactionDao;
        this.currencyDao = currencyDao;
        this.goldPurchaseDao = goldPurchaseDao;
        this.vendorDao = vendorDao;
        this.ledgerTransactionDao = ledgerTransactionDao;
        this.vendorHeaderDao = vendorHeaderDao;
        this.cashBookViewDao = cashBookViewDao;
    }

}
