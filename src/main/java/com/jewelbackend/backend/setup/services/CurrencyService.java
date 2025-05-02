package com.jewelbackend.backend.setup.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.CurrencyRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CurrencyResponseDTO;
import com.jewelbackend.backend.setup.models.Currency;

@Service
public class CurrencyService extends BaseService {

    public CurrencyService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                           AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<CurrencyResponseDTO> getAllCurrency(int size, int page, String search) throws ParseException {

        CriteriaFilter<Currency> criteriaFilter = new CriteriaFilter<>();

        List<Currency> currency = new ArrayList<>();
        if (search.isBlank()) {
            if(size == 0)
                currency = (List<Currency>) getDaoFactory().getCurrencyDao().findAll();
            else{
                PageRequest pageRequest = PageRequest.of(page, size);
                Page<Currency> currencysPage = getDaoFactory().getCurrencyDao()
                        .findAll(pageRequest);
                currency = currencysPage.getContent();
            }
        }else{
            currency = criteriaFilter.getEntitiesByCriteria(Currency.class,
                    HelperUtils.listToMap(search), getEntityManager(), size, page);
        }
        return currency.stream()
                .map(e -> getMapperFactory().getCurrencyMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public CurrencyResponseDTO saveCurrency(
            CurrencyRequestDTO currencyRequestDTO) {
        Currency currency = getMapperFactory().getCurrencyMapper()
                .requestToDomain(currencyRequestDTO);
        currency = getDaoFactory().getCurrencyDao().save(currency);
        return getMapperFactory().getCurrencyMapper().domainToResponse(currency);
    }


    public void deleteCurrency(int id) throws NotPresentException {
        Optional<Currency> currency = getDaoFactory().getCurrencyDao().findById(id);
        if (!currency.isPresent())
            throw new NotPresentException("Currency  is not present");
        getDaoFactory().getCurrencyDao().delete(currency.get());
    }

}
