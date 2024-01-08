package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.MetalTypeRequestDTO;
import com.jewelbackend.backend.setup.dto.response.MetalTypeResponseDTO;
import com.jewelbackend.backend.setup.models.MetalType;

@Component
public class MetalTypeMapper {
    public MetalTypeResponseDTO domainToResponse(MetalType metalType) {
        MetalTypeResponseDTO metalTypeResponseDTO = new MetalTypeResponseDTO();
        metalTypeResponseDTO.setMetalName(metalType.getMetalName());
        metalTypeResponseDTO.setPricePerGram(metalType.getPricePerGram());
        return metalTypeResponseDTO;
    }

    public MetalType requestToDomain(MetalTypeRequestDTO metalTypeRequestDTO) {
        MetalType metalType = new MetalType();
        metalType.setActiveFlag(metalTypeRequestDTO.getActiveFlag());
        metalType.setCreatedDate(metalTypeRequestDTO.getCreatedDate());
        metalType.setMetalName(metalTypeRequestDTO.getMetalName());
        metalType.setCreatedName(metalTypeRequestDTO.getCreatedName());
        metalType.setPricePerGram(metalTypeRequestDTO.getPricePerGram());
        return metalType;
    }
}
