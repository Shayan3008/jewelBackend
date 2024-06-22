package com.jewelbackend.backend.common.validator;

import com.jewelbackend.backend.setup.dao.VendorHeaderDao;
import com.jewelbackend.backend.setup.models.VendorHeader;
import org.springframework.stereotype.Component;

@Component
public class VendorHeaderValidator {

    private final VendorHeaderDao vendorHeaderDao;

    public VendorHeaderValidator(VendorHeaderDao vendorHeaderDao) {
        this.vendorHeaderDao = vendorHeaderDao;
    }
    public String validateVendorHeader(VendorHeader vendorHeader) {
        if (vendorHeader.getName() == null || vendorHeader.getName().isEmpty())
            return "Vendor Header name. cannot be null or empty";
        if (vendorHeaderDao.findByName(vendorHeader.getName()) != null)
            return "Vendor Header with this name already exists";
        return null;
    }
}
