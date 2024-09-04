package com.jewelbackend.backend.setup.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.MetalTypeRequestDTO;
import com.jewelbackend.backend.setup.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public void saveMetalType(MetalTypeRequestDTO metalTypeRequestDTO) throws AlreadyPresentException {
        if(getDaoFactory().getMetalTypeDao().findById(metalTypeRequestDTO.getMetalName()).isPresent())
            throw new AlreadyPresentException("Metal Name already Exists");
        MetalType metalType = getMapperFactory().getMetalTypeMapper().requestToDomain(metalTypeRequestDTO);
        getDaoFactory().getMetalTypeDao().save(metalType);
    }


    public void deleteMetalType(String id) throws NotPresentException {
        var metalType = getDaoFactory().getMetalTypeDao().findById(id);
        if(metalType.isEmpty())
            throw new NotPresentException("Metal Type " + id +" not found");
        getDaoFactory().getMetalTypeDao().delete(metalType.get());
    }

    public List<MetalTypeResponseDTO> getAllMetalTypes(int page, int size, String search) throws ParseException {
        List<MetalType> metalTypes = null;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (search.isEmpty()) {
            Page<MetalType> metalTypePage = getDaoFactory().getMetalTypeDao().findAll(pageRequest);
            metalTypes = metalTypePage.getContent();
        }else{
            CriteriaFilter<MetalType> criteriaFilter = new CriteriaFilter<>();
            metalTypes = criteriaFilter.getEntitiesByCriteriaForSearch(MetalType.class, HelperUtils.listToMap(search), getEntityManager(), size, page, new ArrayList<>());
        }
        return metalTypes.stream().map(e -> {
            return getMapperFactory().getMetalTypeMapper().domainToResponse(e);
        }).toList();
    }
}
