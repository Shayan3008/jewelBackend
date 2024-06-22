package com.jewelbackend.backend.factorybeans;

import com.jewelbackend.backend.common.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ValidatorFactory {

    @Autowired
    UserValidator userValidator;

    @Autowired
    KarigarValidator karigarValidator;

    @Autowired
    CategoryValidator categoryValidator;

    @Autowired
    com.jewelbackend.backend.common.validator.ItemValidator ItemValidator;

    @Autowired
    VendorHeaderValidator vendorHeaderValidator;
}
