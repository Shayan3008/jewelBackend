package com.jewelbackend.backend.setup.mapper;

import com.jewelbackend.backend.setup.dto.request.VendorHeaderRequestDTO;
import com.jewelbackend.backend.setup.dto.response.VendorHeaderResponseDTO;
import com.jewelbackend.backend.setup.models.VendorHeader;
import org.springframework.stereotype.Component;

@Component
public class VendorHeaderMapper {
    public VendorHeader requestToDomain(VendorHeaderRequestDTO vendorHeaderRequestDTO){
        VendorHeader vendorHeader = new VendorHeader();
        vendorHeader.setName(vendorHeaderRequestDTO.getName());
        vendorHeader.setDescription(vendorHeaderRequestDTO.getDescription());
        return vendorHeader;
    }

    public VendorHeaderResponseDTO domainToResponse(VendorHeader vendorHeader){
        VendorHeaderResponseDTO vendorHeaderResponseDTO = new VendorHeaderResponseDTO();
        vendorHeaderResponseDTO.setId(vendorHeader.getId());
        vendorHeaderResponseDTO.setName(vendorHeader.getName());
        vendorHeaderResponseDTO.setDescription(vendorHeader.getDescription());
        return vendorHeaderResponseDTO;
    }
}
