package com.jewelbackend.backend.setup.services;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.LedgerTransactionDto;
import com.jewelbackend.backend.setup.models.LedgerTransaction;
import com.jewelbackend.backend.setup.models.Vendor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LedgerTransactionService extends BaseService {
    public LedgerTransactionService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory, AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public void saveLedger(LedgerTransactionDto ledgerTransactionDto) {
        List<LedgerTransaction> ledgerTransactions = dtoToDomain(ledgerTransactionDto);
        for (var ledgerTransaction : ledgerTransactions) {
            this.daoFactory.getLedgerTransactionDao().save(ledgerTransaction);
        }
    }

    private List<LedgerTransaction> dtoToDomain(LedgerTransactionDto ledgerTransactionDto) {
        List<LedgerTransaction> ledgerTransactions = new ArrayList<>();
        LedgerTransaction ledgerTransaction = new LedgerTransaction();
        LedgerTransaction ledgerTransaction2 = new LedgerTransaction();
        if (ledgerTransactionDto.getTrnType().equals(Constants.SALE_CASH)) {
            if (ledgerTransactionDto.getGoldWeight() == null) {
                ledgerTransaction.setCredit(ledgerTransactionDto.getCredit());
            } else {
                LedgerTransaction ledgerTransaction1 = this.daoFactory.getLedgerTransactionDao().getCreditGoldWeight();
                BigDecimal goldWeight = null;
                if (ledgerTransaction1 != null)
                    goldWeight = ledgerTransaction1.getCreditGoldWeight().subtract(ledgerTransactionDto.getGoldWeight());
                else
                    goldWeight = ledgerTransactionDto.getGoldWeight();
                ledgerTransaction.setCreditGoldWeight(goldWeight);
                ledgerTransaction2.setDebit(ledgerTransactionDto.getCredit());
            }
        } else {
            if (ledgerTransactionDto.getGoldWeight() == null) {
                ledgerTransaction.setDebit(ledgerTransactionDto.getDebit());
            } else {
                LedgerTransaction ledgerTransaction1 = this.daoFactory.getLedgerTransactionDao().getDebitGoldWeight();
                BigDecimal goldWeight = null;
                if (ledgerTransaction1 != null)
                    goldWeight = ledgerTransaction1.getDebitGoldWeight().add(ledgerTransactionDto.getGoldWeight());
                else
                    goldWeight = ledgerTransactionDto.getGoldWeight();
                ledgerTransaction.setDebitGoldWeight(goldWeight);
                ledgerTransaction2.setCredit(ledgerTransactionDto.getDebit());
            }
        }
        Optional<Vendor> vendor = this.daoFactory.getVendorDao().findById(ledgerTransactionDto.getVendorId());
        if (vendor.isPresent()) {
            ledgerTransaction.setVendor(vendor.get());
            ledgerTransaction2.setVendor(vendor.get());
        }
        ledgerTransaction.setTrnDate(ledgerTransactionDto.getDate());
        ledgerTransaction2.setTrnDate(ledgerTransactionDto.getDate());
        ledgerTransactions.add(ledgerTransaction);
        if (ledgerTransactionDto.getGoldWeight() != null)
            ledgerTransactions.add(ledgerTransaction2);
        return ledgerTransactions;
    }
}
