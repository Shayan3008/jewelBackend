package com.jewelbackend.backend.setup.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.KarigarRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.dto.response.KarigarResponseDTO;
import com.jewelbackend.backend.setup.models.Item;
import com.jewelbackend.backend.setup.models.Karigar;

@Service
public class KarigarService extends BaseService {

    public KarigarService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                          AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);

    }

    public List<KarigarResponseDTO> getAllKarigarsLOV() {

        var karigar = (List<Karigar>) this.daoFactory.getKarigarDao().findAll();
        return karigar.stream().map(e -> {
            KarigarResponseDTO karigarResponseDTO = new KarigarResponseDTO();
            karigarResponseDTO = getMapperFactory().getKarigarMapper().domainToResponse(e);
            return karigarResponseDTO;
        }).collect(Collectors.toList());

    }
    public List<KarigarResponseDTO> getAllKarigars(int page, int size, String search) throws ParseException {
        CriteriaFilter<Karigar> criteriaFilter = new CriteriaFilter<>();
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Karigar> karigar = new ArrayList<>();
        if (search.isBlank()) {
            Page<Karigar> karigarPage = getDaoFactory().getKarigarDao().findAll(pageRequest);
            karigar = karigarPage.getContent();
        } else {
            Map<String,String> map = HelperUtils.listToMap(search);
            karigar = criteriaFilter.getEntitiesByCriteriaForSearch(Karigar.class, map, getEntityManager(), size, page,new ArrayList<>());
        }
        return karigar.stream().map(e -> {
            KarigarResponseDTO karigarResponseDTO = new KarigarResponseDTO();
            karigarResponseDTO = getMapperFactory().getKarigarMapper().domainToResponse(e);
            List<Item> items = e.getItems();
            if (items != null) {
                List<ItemResponseDTO> itemResponseDTOs = items.stream()
                        .map(e2 -> getMapperFactory().getItemMapper().domainToResponse(e2))
                        .collect(Collectors.toList());
                karigarResponseDTO.setItemRequestDTOs(itemResponseDTOs);
            }
            return karigarResponseDTO;
        }).collect(Collectors.toList());

    }

    public KarigarResponseDTO saveKarigar(KarigarRequestDTO karigarRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        Karigar karigar = getMapperFactory().getKarigarMapper().requestToDomain(karigarRequestDTO);
        String validKarigar = getValidatorFactory().getKarigarValidator().validateKarigar(karigar);
        if (!Objects.isNull(validKarigar)) {
            throw new InvalidInputException(validKarigar);
        }
        List<Karigar> karigars = getDaoFactory().getKarigarDao().findByKarigarName(karigarRequestDTO.getKarigarName());
        if (!karigars.isEmpty()) {
            throw new AlreadyPresentException(
                    "Karigar with name " + karigarRequestDTO.getKarigarName() + " already exists!");
        }
        karigar = getDaoFactory().getKarigarDao().save(karigar);
        return getMapperFactory().getKarigarMapper().domainToResponse(karigar);
    }

    public KarigarResponseDTO updateKarigar(KarigarRequestDTO karigarRequestDTO)
            throws NotPresentException, InvalidInputException {
        Karigar karigar = getDaoFactory().getKarigarDao().findById(karigarRequestDTO.getId()).orElse(null);
        if (karigar == null)
            throw new NotPresentException("Karigar Not found");
        Karigar updatedKarigar = getMapperFactory().getKarigarMapper().requestToDomain(karigarRequestDTO);
        String validKarigar = getValidatorFactory().getKarigarValidator().validateKarigar(updatedKarigar);
        if (!Objects.isNull(validKarigar)) {
            throw new InvalidInputException(validKarigar);
        }

        getDaoFactory().getKarigarDao().save(updatedKarigar);
        return getMapperFactory().getKarigarMapper().domainToResponse(updatedKarigar);
    }

    public void deleteKarigar(String id) throws NotPresentException {
        Karigar karigar = getDaoFactory().getKarigarDao().findById(Integer.parseInt(id)).orElse(null);
        if (karigar == null)
            throw new NotPresentException(String.format("Karigar with %s not found", id));
        getDaoFactory().getKarigarDao().delete(karigar);
    }

}
