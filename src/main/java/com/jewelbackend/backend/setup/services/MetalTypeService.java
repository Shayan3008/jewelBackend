package com.jewelbackend.backend.setup.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.dto.response.MetalTypeResponseDTO;
import com.jewelbackend.backend.setup.models.MetalType;

@Service
public class MetalTypeService extends BaseService {
    public MetalTypeService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        
    }

    public List<MetalTypeResponseDTO> getAllMetalTypes() {
        List<MetalType> metalTypes = (List<MetalType>) getDaoFactory().getMetalTypeDao().findAll();
        return metalTypes.stream().map(e -> {
            List<CategoryResponseDTO> categoryResponseDTO = e.getCategories().stream()
                    .map(category -> getMapperFactory().getCategoryMapper().domainToResponse(category))
                    .collect(Collectors.toList());
            MetalTypeResponseDTO metalTypeResponseDTO = getMapperFactory().getMetalTypeMapper().domainToResponse(e);
            metalTypeResponseDTO.setCategoryResponseDTOs(categoryResponseDTO);
            return metalTypeResponseDTO;
        }).toList();
    }
}
