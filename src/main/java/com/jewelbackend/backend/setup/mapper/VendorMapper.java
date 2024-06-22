package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.VendorRequestDTO;
import com.jewelbackend.backend.setup.models.Vendor;

@Component
public class VendorMapper {

    public Vendor requestToDomain(VendorRequestDTO vendorRequestDTO) {
        Vendor vendor = new Vendor();
        vendor.setId(vendorRequestDTO.getId());
        vendor.setName(vendorRequestDTO.getVendorName());
        return vendor;
    }
}
