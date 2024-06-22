package com.jewelbackend.backend.setup.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.setup.models.CashBookView;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CashBookViewService extends BaseService {
    public CashBookViewService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public String createCashBookFromView() {
        List<CashBookView> cashBookViews = (List<CashBookView>) daoFactory.getCashBookViewDao().findAll();
        var finalBalance = BigDecimal.ZERO;
        for (var i = 0; i < cashBookViews.size(); i++) {
            var cashBookView = cashBookViews.get(i);
            var cashBook = mapperFactory.getCashBookMapper().mapCashBookViewToCashBook(cashBookView);
            if (i > 0) {
                var previousCashBook = cashBookViews.get(i - 1);
                cashBook.setOpeningBalance(finalBalance);
                if(cashBookView.getType1().equals(Constants.SALE_CASH))
                    cashBook.setFinalBalance(finalBalance.add(cashBook.getAmount()));
                else
                    cashBook.setFinalBalance(finalBalance.subtract(cashBook.getAmount()));
            }else{
                cashBook.setOpeningBalance(finalBalance);
                cashBook.setFinalBalance(cashBook.getAmount());
            }
            finalBalance = cashBook.getFinalBalance();
            daoFactory.getCashBookDao().save(cashBook);
        }
        return "Done";
    }
}
