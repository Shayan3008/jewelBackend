package com.jewelbackend.backend.sale.mapper;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.sale.dto.request.CurrencyTransactionRequestDTO;
import com.jewelbackend.backend.sale.dto.response.CurrencyTransactionResponseDTO;
import com.jewelbackend.backend.sale.model.CurrencyTransaction;
import com.jewelbackend.backend.setup.models.CashBook;

@Component
public class CurrencyTransactionMapper {
    public CurrencyTransaction requestToDomain(CurrencyTransactionRequestDTO currencyTransactionRequestDTO) {
        CurrencyTransaction currencyTransaction = new CurrencyTransaction();
        currencyTransaction.setId(currencyTransactionRequestDTO.getId());
        currencyTransaction.setTrnType(currencyTransactionRequestDTO.getTrnType());
        currencyTransaction.setAmount(currencyTransactionRequestDTO.getAmount());
        currencyTransaction.setExchangeRate(currencyTransactionRequestDTO.getExchangeRate());
        currencyTransaction.setDescription(currencyTransactionRequestDTO.getDescription());
        currencyTransaction.setQty(currencyTransactionRequestDTO.getQty());
        currencyTransaction.setTransactionDate(new Date());
        return currencyTransaction;
    }

    public CurrencyTransactionResponseDTO domainToResponse(CurrencyTransaction currencyTransaction) {
        CurrencyTransactionResponseDTO currencyTransactionResponseDTO = new CurrencyTransactionResponseDTO();
        currencyTransactionResponseDTO.setId(currencyTransaction.getId());
        currencyTransactionResponseDTO.setTrnType(currencyTransaction.getTrnType());
        currencyTransactionResponseDTO.setDescription(currencyTransaction.getDescription());
        currencyTransactionResponseDTO.setAmount(currencyTransaction.getAmount());
        currencyTransactionResponseDTO.setCurrencyName(currencyTransaction.getCurrency().getCurrencyName());
        currencyTransactionResponseDTO.setExchangeRate(currencyTransaction.getExchangeRate());
        currencyTransactionResponseDTO.setCurrencyId(currencyTransaction.getCurrency().getId());
        currencyTransactionResponseDTO.setQty(currencyTransaction.getQty());
        currencyTransactionResponseDTO.setPresentAmount(currencyTransaction.getCurrency().getPresentAmount());
        return currencyTransactionResponseDTO;
    }

    public CashBook currencyTransactionToCashBook(CurrencyTransaction currencyTransaction){
        CashBook cashBook = new CashBook();
        cashBook.setTrnDate(currencyTransaction.getTransactionDate());
        cashBook.setTrnType(currencyTransaction.getTrnType());
        cashBook.setAmount(currencyTransaction.getAmount());
        return cashBook;
    }
}
