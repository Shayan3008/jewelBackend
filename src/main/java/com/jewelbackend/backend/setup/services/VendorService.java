package com.jewelbackend.backend.setup.services;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.LedgerTransactionDto;
import com.jewelbackend.backend.setup.dto.response.LedgerTransactionUpdateDto;
import com.jewelbackend.backend.setup.models.LedgerTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.VendorRequestDTO;
import com.jewelbackend.backend.setup.models.Vendor;

@Service
public class VendorService extends BaseService {

    public VendorService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                         AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<VendorRequestDTO> getAllVendors(int page, int size, String search) throws ParseException {
        CriteriaFilter<Vendor> criteriaFilter = new CriteriaFilter<>();
        PageRequest pageRequest = PageRequest.of(page,size);
        List<Vendor> vendors = new ArrayList<>();
        if (!search.isBlank()) {
            vendors = criteriaFilter.getEntitiesByCriteriaForSearch(Vendor.class, HelperUtils.listToMap(search), getEntityManager(), size, page, new ArrayList<>());

        } else {
            Page<Vendor> vendorPage = getDaoFactory().getVendorDao().findAll(pageRequest);
            vendors =  vendorPage.getContent();
        }
        List<VendorRequestDTO> vendorRequestDTOS = new ArrayList<>();
        for(var vendor: vendors){
            VendorRequestDTO vendorRequestDTO = new VendorRequestDTO();
            vendorRequestDTO.setId(vendor.getId());
            vendorRequestDTO.setVendorName(vendor.getName());
            vendorRequestDTO.setVendorHeaderId(vendor.getVendorHeader().getId());
            vendorRequestDTO.setVendorHeaderName(vendor.getVendorHeader().getName());
            vendorRequestDTOS.add(vendorRequestDTO);
        }
        return vendorRequestDTOS;
    }

    public Vendor saveVendor(VendorRequestDTO vendorRequestDTO) {
        Vendor vendor = this.mapperFactory.getVendorMapper().requestToDomain(vendorRequestDTO);
        this.daoFactory.getVendorHeaderDao().findById(vendorRequestDTO.getVendorHeaderId()).ifPresent(vendor::setVendorHeader);
        return this.daoFactory.getVendorDao().save(vendor);
    }

    public LedgerTransactionUpdateDto getGoldTransactionForVendor(int id) throws NotPresentException {
        Optional<Vendor> vendor = this.daoFactory.getVendorDao().findById(id);
        if (vendor.isEmpty()) {
            throw new NotPresentException("Vendor not found");
        }
        Vendor vendor1 = vendor.get();
        LedgerTransaction ledgerTransactionCredit = this.daoFactory.getLedgerTransactionDao().getCreditGoldWeightByVendor(vendor1);
        LedgerTransaction ledgerTransactionDebit = this.daoFactory.getLedgerTransactionDao().getDebitGoldWeightByVendor(vendor1);
        return getLedgerTransactionUpdateDto(ledgerTransactionDebit, ledgerTransactionCredit);
    }

    private static LedgerTransactionUpdateDto getLedgerTransactionUpdateDto(LedgerTransaction ledgerTransactionDebit, LedgerTransaction ledgerTransactionCredit) {
        LedgerTransactionUpdateDto ledgerTransactionUpdateDto = new LedgerTransactionUpdateDto();
        if (ledgerTransactionDebit == null)
            ledgerTransactionUpdateDto.setDebitGoldWeight(BigDecimal.ZERO);
        else
            ledgerTransactionUpdateDto.setDebitGoldWeight(ledgerTransactionDebit.getDebitGoldWeight());
        if (ledgerTransactionCredit == null)
            ledgerTransactionUpdateDto.setCreditGoldWeight(BigDecimal.ZERO);
        else
            ledgerTransactionUpdateDto.setCreditGoldWeight(ledgerTransactionCredit.getCreditGoldWeight());
        return ledgerTransactionUpdateDto;
    }

    public LedgerTransactionDto saveLedger(LedgerTransactionDto ledgerTransactionDto) throws NotPresentException {
        LedgerTransaction ledgerTransaction = new LedgerTransaction();
        LedgerTransaction newWeightLedgerTransaction = new LedgerTransaction();
        Optional<Vendor> vendor = this.daoFactory.getVendorDao().findById(ledgerTransactionDto.getVendorId());
        if (vendor.isEmpty()) {
            throw new NotPresentException("Vendor not found");
        }
        ledgerTransaction.setVendor(vendor.get());
        newWeightLedgerTransaction.setVendor(vendor.get());

        if (ledgerTransactionDto.getTrnType().equals(Constants.SALE_CASH)) {
            LedgerTransaction ledgerTransaction1 = this.daoFactory.getLedgerTransactionDao().getCreditGoldWeightByVendor(vendor.get());

            BigDecimal creditGoldWeight = ledgerTransaction1.getCreditGoldWeight().subtract(ledgerTransactionDto.getGoldWeight());
            newWeightLedgerTransaction.setCreditGoldWeight(creditGoldWeight);
            ledgerTransaction.setCredit(ledgerTransactionDto.getAmount());

        } else {
            LedgerTransaction ledgerTransaction1 = this.daoFactory.getLedgerTransactionDao().getDebitGoldWeightByVendor(vendor.get());
            BigDecimal debitGoldWeight = ledgerTransaction1.getDebitGoldWeight().subtract(ledgerTransactionDto.getGoldWeight());
            newWeightLedgerTransaction.setDebitGoldWeight(debitGoldWeight);
            ledgerTransaction.setDebit(ledgerTransactionDto.getAmount());
        }
        ledgerTransaction.setTrnDate(new Date());
        newWeightLedgerTransaction.setTrnDate(new Date());
        this.daoFactory.getLedgerTransactionDao().save(ledgerTransaction);
        this.daoFactory.getLedgerTransactionDao().save(newWeightLedgerTransaction);
        return ledgerTransactionDto;
    }

    public List<VendorRequestDTO> getVendorByVendorHeader(int id) throws NotPresentException {
        var vendorHeaders = this.daoFactory.getVendorHeaderDao().findById(id).orElseThrow(()->new NotPresentException("Vendor Header not found"));
        List<Vendor> vendors = vendorHeaders.getVendors();
        List<VendorRequestDTO> vendorRequestDTOS = new ArrayList<>();
        for(var vendor: vendors){
            VendorRequestDTO vendorRequestDTO = new VendorRequestDTO();
            vendorRequestDTO.setId(vendor.getId());
            vendorRequestDTO.setVendorName(vendor.getName());
            vendorRequestDTO.setVendorHeaderId(vendor.getVendorHeader().getId());
            vendorRequestDTO.setVendorHeaderName(vendor.getVendorHeader().getName());
            vendorRequestDTOS.add(vendorRequestDTO);
        }
        return vendorRequestDTOS;
    }
}
