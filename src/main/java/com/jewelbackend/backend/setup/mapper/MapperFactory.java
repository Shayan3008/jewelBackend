package com.jewelbackend.backend.setup.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
