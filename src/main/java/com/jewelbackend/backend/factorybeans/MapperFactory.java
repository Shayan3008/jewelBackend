package com.jewelbackend.backend.factorybeans;

import com.jewelbackend.backend.setup.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.sale.mapper.CurrencyTransactionMapper;

import lombok.Getter;

@Component
@Getter
public class MapperFactory {

    @Autowired
    UserMapper userMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    MetalTypeMapper metalTypeMapper;

    @Autowired
    SubCategoryMapper subCategoryMapper;

    @Autowired
    KarigarMapper karigarMapper;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    InvoiceMapper invoiceMapper;

    @Autowired
    CashBookMapper cashBookMapper;

    @Autowired
    CurrencyTransactionMapper currencyTransactionMapper;

    @Autowired
    CurrencyMapper currencyMapper;

    @Autowired
    GoldPurchaseMapper goldPurchaseMapper;

    @Autowired
    VendorMapper vendorMapper;

    @Autowired
    VendorHeaderMapper vendorHeaderMapper;
}
