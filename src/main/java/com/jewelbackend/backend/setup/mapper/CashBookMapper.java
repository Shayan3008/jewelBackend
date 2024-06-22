package com.jewelbackend.backend.setup.mapper;


import com.jewelbackend.backend.setup.models.CashBookView;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.sale.model.CurrencyTransaction;
import com.jewelbackend.backend.setup.dto.request.CashBookRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CashBookResponseDTO;
import com.jewelbackend.backend.setup.models.CashBook;

@Component
public class CashBookMapper {
    public CashBook requestToDomain(CashBookRequestDTO cashBookRequestDTO) {
        CashBook cashBook = new CashBook();
        cashBook.setAmount(cashBookRequestDTO.getAmount());
        cashBook.setTrnDate(cashBookRequestDTO.getTrnDate());
        cashBook.setTrnType(cashBookRequestDTO.getTrnType());
        return cashBook;
    }

    public CashBookResponseDTO domainToResponse(CashBook cashBook) {
        CashBookResponseDTO cashBookResponseDTO = new CashBookResponseDTO();
        cashBookResponseDTO.setAmount(cashBook.getAmount());
        cashBookResponseDTO.setTrnDate(cashBook.getTrnDate());
        cashBookResponseDTO.setTrnType(cashBook.getTrnType());
        return cashBookResponseDTO;
    }

    public CashBook currencyTransactionToCashBook(CurrencyTransaction currencyTransaction) {
        CashBook cashBook = new CashBook();
        cashBook.setAmount(currencyTransaction.getAmount());
        cashBook.setTrnType(currencyTransaction.getTrnType());
        cashBook.setTrnDate(currencyTransaction.getTransactionDate());
        return cashBook;
    }

    public CashBook mapCashBookViewToCashBook(CashBookView cashBookView) {
        CashBook cashBook = new CashBook();
        cashBook.setAmount(cashBookView.getAmount());
        cashBook.setTrnDate(cashBookView.getDated());
        cashBook.setTrnType(cashBookView.getType1());
        return cashBook;
    }
}
