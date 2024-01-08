package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.KarigarRequestDTO;
import com.jewelbackend.backend.setup.dto.response.KarigarResponseDTO;
import com.jewelbackend.backend.setup.models.Karigar;

@Component
public class KarigarMapper {
    public KarigarResponseDTO domainToResponse(Karigar karigar) {
        KarigarResponseDTO karigarResponseDTO = new KarigarResponseDTO();
        karigarResponseDTO.setId(karigar.getId());
        karigarResponseDTO.setKarigarName(karigar.getKarigarName());
        karigarResponseDTO.setDescription(karigar.getDescription());
        return karigarResponseDTO;
    }

    public Karigar requestToDomain(KarigarRequestDTO karigarRequestDTO) {
        Karigar karigar = new Karigar();
        karigar.setId(karigarRequestDTO.getId());
        karigar.setKarigarName(karigarRequestDTO.getKarigarName());
        karigar.setDescription(karigarRequestDTO.getDescription());
        return karigar;
    }
}
