package com.jewelbackend.backend.setup.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.response.VendorHeaderResponseDTO;
import com.jewelbackend.backend.setup.models.VendorHeader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendorHeaderService extends BaseService {
    public VendorHeaderService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<VendorHeaderResponseDTO> findAllVendorHeaders(int page, int size, String search) throws ParseException {
        CriteriaFilter<VendorHeader> criteriaFilter = new CriteriaFilter<>();
        PageRequest pageRequest = PageRequest.of(page, size);
        if (!search.isBlank()) {
            return criteriaFilter.getEntitiesByCriteriaForSearch(VendorHeader.class, HelperUtils.listToMap(search), getEntityManager(), size, page, new ArrayList<>())
                    .stream().map(this.mapperFactory.getVendorHeaderMapper()::domainToResponse).toList();
        } else {
            Page<VendorHeader> vendorPage = getDaoFactory().getVendorHeaderDao().findAll(pageRequest);
            return vendorPage.getContent().stream().map(this.mapperFactory.getVendorHeaderMapper()::domainToResponse).toList();
        }
    }

    public VendorHeaderResponseDTO saveVendorHeader(VendorHeader vendorHeader) {
        String validate = this.validatorFactory.getVendorHeaderValidator().validateVendorHeader(vendorHeader);
        if (validate != null) {
            throw new IllegalArgumentException(validate);
        }
        return this.mapperFactory.getVendorHeaderMapper().domainToResponse(this.daoFactory.getVendorHeaderDao().save(vendorHeader));
    }

    public VendorHeaderResponseDTO getVendorHeaderById(int id) {
        return this.mapperFactory.getVendorHeaderMapper().domainToResponse(this.daoFactory.getVendorHeaderDao().findById(id).get());
    }

    public VendorHeaderResponseDTO updateVendorHeader(VendorHeader vendorHeader) {
        return this.mapperFactory.getVendorHeaderMapper().domainToResponse(this.daoFactory.getVendorHeaderDao().save(vendorHeader));
    }

    public void deleteVendorHeader(int id) {
        this.daoFactory.getVendorHeaderDao().deleteById(id);
    }
}
