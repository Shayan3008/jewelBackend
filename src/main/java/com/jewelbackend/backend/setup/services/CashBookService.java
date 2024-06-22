package com.jewelbackend.backend.setup.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.constants.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.response.CashBookResponseDTO;
import com.jewelbackend.backend.setup.models.CashBook;

@Service
public class CashBookService extends BaseService {

    public CashBookService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<CashBookResponseDTO> getAllCashBookByTransactionType(String type, int page, int size)
            throws ParseException {
        List<CashBook> cashBooks = null;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (type.isBlank()) {
            Page<CashBook> pageCashBook = getDaoFactory().getCashBookDao().findAll(pageRequest);
            cashBooks = pageCashBook.getContent();
        } else {
            String[] filters = type.split(",");
            Date date = null;
            if (!filters[1].equals("%")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD");
                date = simpleDateFormat.parse(filters[1]);
            }
            if (filters[1].equals("%")) {
                Page<CashBook> pageCashBook = getDaoFactory().getCashBookDao().findAllByTrnType(filters[0],
                        pageRequest);
                cashBooks = pageCashBook.getContent();
            } else if (filters[0].equals("%")) {
                Page<CashBook> pageCashBook = getDaoFactory().getCashBookDao().findAllByTrnDate(date, pageRequest);
                cashBooks = pageCashBook.getContent();
            } else {
                Page<CashBook> pageCashBook = getDaoFactory().getCashBookDao().findAllByTrnDateAndTrnType(filters[0],
                        date, pageRequest);
                cashBooks = pageCashBook.getContent();
            }
        }
        return cashBooks.stream().map(e -> getMapperFactory().getCashBookMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public CashBook saveCashBook(CashBook cashBook) {
        List<CashBook> cashBooks = this.daoFactory.getCashBookDao().findLastTransaction(cashBook.getTrnDate());

        CashBook cashBook1 = null;
        if (!cashBooks.isEmpty())
            cashBook1 = cashBooks.get(0);
        updatedOpeningBalance(cashBook1 == null ? null : cashBook1.getFinalBalance(), cashBook);
        cashBook = this.daoFactory.getCashBookDao().save(cashBook);
        return cashBook;
    }

    private void updatedOpeningBalance(BigDecimal openingBalance, CashBook cashBook) {
        if (openingBalance == null) {
            openingBalance = BigDecimal.ZERO;
        }
        BigDecimal finalBalance = null;
        if (cashBook.getTrnType().equals(Constants.SALE_CASH)) {
            finalBalance = openingBalance.add(cashBook.getAmount());
        } else {
            finalBalance = openingBalance.subtract(cashBook.getAmount());
        }
        cashBook.setOpeningBalance(openingBalance);
        cashBook.setFinalBalance(finalBalance);
    }

}
