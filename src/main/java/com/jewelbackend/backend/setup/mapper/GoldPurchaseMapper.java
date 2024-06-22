package com.jewelbackend.backend.setup.mapper;

import com.jewelbackend.backend.setup.dto.request.LedgerTransactionDto;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.setup.dto.request.GoldPurchaseRequestDto;
import com.jewelbackend.backend.setup.dto.response.GoldPurchaseResponseDto;
import com.jewelbackend.backend.setup.models.CashBook;
import com.jewelbackend.backend.setup.models.GoldPurchase;

@Component
public class GoldPurchaseMapper {

    public GoldPurchase requestToDomain(GoldPurchaseRequestDto goldPurchaseRequestDto) {
        GoldPurchase goldPurchase = new GoldPurchase();
        goldPurchase.setId(goldPurchaseRequestDto.getId());
        goldPurchase.setAmount(goldPurchaseRequestDto.getAmount());
        goldPurchase.setDescription(goldPurchaseRequestDto.getDescription());
        goldPurchase.setGoldPurity(goldPurchaseRequestDto.getGoldPurity());
        goldPurchase.setGoldWeight(goldPurchaseRequestDto.getGoldWeight());
        goldPurchase.setPurchaseDate(goldPurchaseRequestDto.getPurchaseDate());
        goldPurchase.setAmount(goldPurchaseRequestDto.getAmount());
        goldPurchase.setOption1(goldPurchaseRequestDto.getOption());
        goldPurchase.setTrnType(goldPurchaseRequestDto.getTrnType());
        return goldPurchase;
    }

    public GoldPurchaseResponseDto domainToResponse(GoldPurchase goldPurchase) {
        GoldPurchaseResponseDto goldPurchaseResponseDto = new GoldPurchaseResponseDto();
        goldPurchaseResponseDto.setId(goldPurchase.getId());
        goldPurchaseResponseDto.setAmount(goldPurchase.getAmount());
        goldPurchaseResponseDto.setDescription(goldPurchase.getDescription());
        goldPurchaseResponseDto.setGoldPurity(goldPurchase.getGoldPurity());
        goldPurchaseResponseDto.setGoldWeight(goldPurchase.getGoldWeight());
        goldPurchaseResponseDto.setPurchaseDate(goldPurchase.getPurchaseDate());
        goldPurchaseResponseDto.setAmount(goldPurchase.getAmount());
        return goldPurchaseResponseDto;
    }

    public CashBook goldPurchaseToCashBook(GoldPurchase goldPurchase) {
        CashBook cashBook = new CashBook();
        cashBook.setAmount(goldPurchase.getAmount());
        cashBook.setTrnDate(goldPurchase.getPurchaseDate());
        cashBook.setTrnType(goldPurchase.getTrnType());
        return cashBook;
    }

    public LedgerTransactionDto goldPurchaseToledgerTransaction(GoldPurchase goldPurchase) {
        LedgerTransactionDto ledgerTransactionDto = new LedgerTransactionDto();
        ledgerTransactionDto.setGoldRate(goldPurchase.getGoldRate());
        ledgerTransactionDto.setGoldWeight(goldPurchase.getGoldWeight());
        ledgerTransactionDto.setVendorId(goldPurchase.getVendor().getId());
        ledgerTransactionDto.setTrnType(goldPurchase.getTrnType());
        if (goldPurchase.getTrnType().equals(Constants.SALE_CASH)) {
            ledgerTransactionDto.setCredit(goldPurchase.getAmount());

        } else {
            ledgerTransactionDto.setDebit(goldPurchase.getAmount());
        }
        ledgerTransactionDto.setDate(goldPurchase.getPurchaseDate());

        return ledgerTransactionDto;
    }
}
