package com.jewelbackend.backend.setup.mapper;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.VendorRequestDTO;
import com.jewelbackend.backend.setup.models.Vendor;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorMapper {

    public Vendor requestToDomain(VendorRequestDTO vendorRequestDTO) {
        Vendor vendor = new Vendor();
        vendor.setId(vendorRequestDTO.getId());
        vendor.setName(vendorRequestDTO.getVendorName());
        vendor.setCusCode(vendorRequestDTO.getCusCode());
        return vendor;
    }

    public VendorRequestDTO domainToResponse(Vendor vendor) {
        VendorRequestDTO vendorRequestDTO = new VendorRequestDTO();
        vendorRequestDTO.setId(vendor.getId());
        vendorRequestDTO.setVendorName(vendor.getName());
        vendorRequestDTO.setVendorHeaderId(vendor.getVendorHeader().getId());
        vendorRequestDTO.setVendorHeaderName(vendor.getVendorHeader().getName());
        vendorRequestDTO.setCusCode(vendor.getCusCode());
        return vendorRequestDTO;
    }

    public VendorRequestDTO domainToResponseForLov(Vendor e) {
        VendorRequestDTO vendorRequestDTO = new VendorRequestDTO();
        vendorRequestDTO.setId(e.getId());
        vendorRequestDTO.setVendorName(e.getName() + (e.getCusCode() != null ? " | " + e.getCusCode() : ""));
        return vendorRequestDTO;
    }
}
