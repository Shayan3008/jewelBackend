package com.jewelbackend.backend.sale.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.setup.models.CashBook;
import com.jewelbackend.backend.setup.services.CashBookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.sale.dto.request.CurrencyTransactionRequestDTO;
import com.jewelbackend.backend.sale.dto.response.CurrencyTransactionResponseDTO;
import com.jewelbackend.backend.sale.model.CurrencyTransaction;
import com.jewelbackend.backend.setup.models.Currency;
import com.jewelbackend.backend.setup.services.BaseService;

@Service
public class CurrencyTransactionService extends BaseService {

    final CashBookService cashBookService;

    public CurrencyTransactionService(DaoFactory daoFactory, ValidatorFactory validatorFactory,
            MapperFactory mapperFactory, AuthenticationManager authenticationManager,
                                      JwtAuthConfig jwtAuthConfig,CashBookService cashBookService) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        this.cashBookService = cashBookService;
    }

    public List<CurrencyTransactionResponseDTO> getAllCurrencyTransactions(int size, int page,String search) throws ParseException {
        CriteriaFilter<CurrencyTransaction> criteriaFilter = new CriteriaFilter<>();
        List<CurrencyTransaction> currencyTransactions = null;
        if (search.isBlank()) {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<CurrencyTransaction> currencyTransactionsPage = getDaoFactory().getCurrencyTransactionDao()
                    .findAll(pageRequest);
            currencyTransactions = currencyTransactionsPage.getContent();
        } else {
            Map<String,String> map = HelperUtils.listToMap(search);
            currencyTransactions = criteriaFilter.getEntitiesByCriteriaForSearch(CurrencyTransaction.class, map, getEntityManager(), size, page,new ArrayList<>());
        }
        return currencyTransactions.stream()
                .map(e -> getMapperFactory().getCurrencyTransactionMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public CurrencyTransactionResponseDTO saveCurrencyTransaction(
            CurrencyTransactionRequestDTO currencyTransactionRequestDTO) throws NotPresentException {
        CurrencyTransaction currencyTransaction = getMapperFactory().getCurrencyTransactionMapper()
                .requestToDomain(currencyTransactionRequestDTO);
        Optional<Currency> currency = getDaoFactory().getCurrencyDao()
                .findById(currencyTransactionRequestDTO.getCurrencyId());
        if (!currency.isPresent())
            throw new NotPresentException("Currency not found with id");
        Currency currency2 = currency.get();
        if(currency2.getPresentAmount() == null){
            currency2.setPresentAmount(BigDecimal.ZERO);
        }
        if(currencyTransactionRequestDTO.getTrnType().equals(Constants.SALE_CASH)){
            currency2.setPresentAmount(currency2.getPresentAmount().subtract(currencyTransactionRequestDTO.getQty()));
        }
        else{
            currency2.setPresentAmount(currency2.getPresentAmount().add(currencyTransactionRequestDTO.getQty()));
        }
        
        currencyTransaction.setCurrency(currency.get());
        currencyTransaction = getDaoFactory().getCurrencyTransactionDao().save(currencyTransaction);

        CashBook cashBook = getMapperFactory().getCurrencyTransactionMapper().currencyTransactionToCashBook(currencyTransaction);
        this.cashBookService.saveCashBook(cashBook);
        return getMapperFactory().getCurrencyTransactionMapper().domainToResponse(currencyTransaction);
    }



    public void deleteCurrencyTransaction(int id) throws AlreadyPresentException {
        Optional<CurrencyTransaction> currencyTransaction =  getDaoFactory().getCurrencyTransactionDao().findById(id);
        if(!currencyTransaction.isPresent())
            throw new AlreadyPresentException("Currency transaction is not present");
        getDaoFactory().getCurrencyTransactionDao().delete(currencyTransaction.get());
    }
}
