package com.jewelbackend.backend.setup.services;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.GoldPurchaseRequestDto;
import com.jewelbackend.backend.setup.dto.request.LedgerTransactionDto;
import com.jewelbackend.backend.setup.dto.response.GoldPurchaseResponseDto;
import com.jewelbackend.backend.setup.models.CashBook;
import com.jewelbackend.backend.setup.models.GoldPurchase;
import com.jewelbackend.backend.setup.models.Vendor;

import jakarta.transaction.Transactional;

@Service
@Transactional(rollbackOn = Exception.class)
public class GoldPurchaseService extends BaseService {

    final CashBookService cashBookService;
    final LedgerTransactionService ledgerTransactionService;

    public GoldPurchaseService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig,
            CashBookService cashBookService, LedgerTransactionService ledgerTransactionService) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        this.cashBookService = cashBookService;
        this.ledgerTransactionService = ledgerTransactionService;
    }

    public List<GoldPurchaseResponseDto> getAllGoldPurchases(int size, int page, Map<String, String> parameters)
            throws ParseException {
        CriteriaFilter<GoldPurchase> criteriaFilter = new CriteriaFilter<>();
        List<GoldPurchase> goldPurchases = null;
        if (!parameters.isEmpty()) {

            goldPurchases = criteriaFilter.getEntitiesByCriteria(GoldPurchase.class,
                    parameters, getEntityManager(), size, page);
        } else {

            PageRequest pageRequest = PageRequest.of(page, size);
            Page<GoldPurchase> goldPurchasesPage = getDaoFactory().getGoldPurchaseDao()
                    .findAll(pageRequest);
            goldPurchases = goldPurchasesPage.getContent();
        }
        return goldPurchases.stream()
                .map(e -> getMapperFactory().getGoldPurchaseMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public GoldPurchaseResponseDto saveGoldPurchase(GoldPurchaseRequestDto goldPurchaseRequestDto)
            throws NotPresentException {
        GoldPurchase goldPurchase = getMapperFactory().getGoldPurchaseMapper().requestToDomain(goldPurchaseRequestDto);
        Optional<Vendor> vendor = this.daoFactory.getVendorDao().findById(goldPurchaseRequestDto.getVendorId());
        if (vendor.isEmpty())
            throw new NotPresentException("Vendor with id " + goldPurchaseRequestDto.getVendorId() + "not found");
        goldPurchase.setVendor(vendor.get());
        goldPurchase = getDaoFactory().getGoldPurchaseDao().save(goldPurchase);
        CashBook cashBook = getMapperFactory().getGoldPurchaseMapper().goldPurchaseToCashBook(goldPurchase);
        LedgerTransactionDto ledgerTransactionDto = getMapperFactory().getGoldPurchaseMapper()
                .goldPurchaseToledgerTransaction(goldPurchase);
        this.cashBookService.saveCashBook(cashBook);
        this.ledgerTransactionService.saveLedger(ledgerTransactionDto);
        return getMapperFactory().getGoldPurchaseMapper().domainToResponse(goldPurchase);
    }

    public void deleteGoldPurchase(int id) throws NotPresentException {
        Optional<GoldPurchase> goldPurchase = getDaoFactory().getGoldPurchaseDao().findById(id);
        if (!goldPurchase.isPresent()) {
            throw new NotPresentException("Gold Purchase with id is not present.");
        }
        getDaoFactory().getGoldPurchaseDao().delete(goldPurchase.get());
    }

}
