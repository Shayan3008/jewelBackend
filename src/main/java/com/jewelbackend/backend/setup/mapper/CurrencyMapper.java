package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.CurrencyRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CurrencyResponseDTO;
import com.jewelbackend.backend.setup.models.Currency;

@Component
public class CurrencyMapper {
    public Currency requestToDomain(CurrencyRequestDTO currencyRequestDTO){
        Currency currency = new Currency();
        currency.setId(currencyRequestDTO.getId());
        currency.setCurrencyName(currencyRequestDTO.getCurrencyName());
        currency.setDescription(currencyRequestDTO.getDescription());
        return currency;
    }

    public CurrencyResponseDTO domainToResponse(Currency currency){
        CurrencyResponseDTO currencyResponseDTO = new CurrencyResponseDTO();
        currencyResponseDTO.setId(currency.getId());
        currencyResponseDTO.setCurrencyName(currency.getCurrencyName());
        currencyResponseDTO.setDescription(currency.getDescription());
        currencyResponseDTO.setPresentAmount(currency.getPresentAmount());
        return currencyResponseDTO;        
    }
}
