package com.jewelbackend.backend.setup.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.factorybeans.ValidatorFactory;
import com.jewelbackend.backend.factorybeans.DaoFactory;
import com.jewelbackend.backend.factorybeans.MapperFactory;
import com.jewelbackend.backend.setup.dto.request.InvoiceRequestDTO;
import com.jewelbackend.backend.setup.dto.response.InvoiceResponseDto;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.models.CashBook;
import com.jewelbackend.backend.setup.models.Invoice;
import com.jewelbackend.backend.setup.models.Item;

@Service
public class InvoiceService extends BaseService {

    private final CashBookService cashBookService;

    public InvoiceService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
                          AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig, CashBookService cashBookService) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
        this.cashBookService = cashBookService;
    }

    public List<InvoiceResponseDto> findAllInvoices(int page, int size, String search,String filter) throws ParseException {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<Invoice> invoices = null;
        Page<Invoice> invoicPage = null;
        if (search.isBlank() && filter.isBlank()) {

            invoicPage = getDaoFactory().getInvoiceDao().findAll(pageRequest);
            invoices = invoicPage.getContent();
        } else if (!filter.isBlank()){
            CriteriaFilter<Invoice> criteriaFilter = new CriteriaFilter<>();
            invoices = criteriaFilter.getEntitiesByCriteriaForSearch(Invoice.class, HelperUtils.listToMap(filter), getEntityManager(),
                    size,page,new ArrayList<>());
        }
        else {
            search = "%" + search + "%";
            invoicPage = getDaoFactory().getInvoiceDao().findAllByKarigarNameOrCategoryName(search, pageRequest);
            invoices = invoicPage.getContent();
        }
        return invoices.stream().map(e -> getMapperFactory().getInvoiceMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public InvoiceResponseDto saveInvoice(InvoiceRequestDTO invoiceRequestDTO) throws InvalidInputException {
        Invoice invoice = getMapperFactory().getInvoiceMapper().requestToDomain(invoiceRequestDTO);
        Optional<Item> optionalItem = getDaoFactory().getItemDao().findById(invoiceRequestDTO.getItemId());
        if (optionalItem.isEmpty())
            throw new InvalidInputException("Item with id not found");
        Item item = optionalItem.get();
        item.setRemainingNetWeight(item.getNetWeight().subtract(invoiceRequestDTO.getItemWeight()));
        if (item.getQty().compareTo(BigInteger.valueOf(1)) > 0) {
            BigInteger qty = item.getQty().subtract(BigInteger.valueOf(invoiceRequestDTO.getQty()));
            item.setQty(qty);
        }
        invoice.setItem(item);
        invoice = getDaoFactory().getInvoiceDao().save(invoice);
        CashBook cashBook = getMapperFactory().getInvoiceMapper().invoiceToCashBook(invoice);
        this.cashBookService.saveCashBook(cashBook);
        return getMapperFactory().getInvoiceMapper().domainToResponse(invoice);
    }

    public InvoiceResponseDto updateInvoice(InvoiceRequestDTO invoiceRequestDTO)
            throws InvalidInputException, NotPresentException {
        Invoice invoice = getDaoFactory().getInvoiceDao().findById(invoiceRequestDTO.getId()).orElse(null);
        if (invoice == null)
            throw new NotPresentException("Invoice not found.");
        Invoice updatedInvoice = getMapperFactory().getInvoiceMapper().requestToDomain(invoiceRequestDTO);
        Optional<Item> item = getDaoFactory().getItemDao().findById(invoiceRequestDTO.getItemId());
        if (item.isEmpty())
            throw new InvalidInputException("Item with id not found");
        updatedInvoice.setItem(item.get());
        updatedInvoice = getDaoFactory().getInvoiceDao().save(updatedInvoice);
        return getMapperFactory().getInvoiceMapper().domainToResponse(updatedInvoice);
    }

    public void deleteInvoice(String invoiceId) throws NotPresentException {
        Invoice invoice = getDaoFactory().getInvoiceDao().findById(Integer.parseInt(invoiceId)).orElse(null);
        if (invoice == null)
            throw new NotPresentException("Invoice not found.");
        getDaoFactory().getInvoiceDao().delete(invoice);
    }

    public BigDecimal getGoldRate(String date) throws NotPresentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-M-yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        String datedString = HelperUtils.localDateToString(localDate);
        var goldRate = this.getDaoFactory().getGoldRateDao().findByDatedString(datedString);
        if (goldRate.isEmpty()) {
            throw new NotPresentException("Rate against this date not present");
        }
        return goldRate.get(0).getPrice();

    }

    public InvoiceResponseDto saveInvoiceWithoutItem(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = getMapperFactory().getInvoiceMapper().requestToDomain(invoiceRequestDTO);
        invoice = getDaoFactory().getInvoiceDao().save(invoice);
        CashBook cashBook = getMapperFactory().getInvoiceMapper().invoiceToCashBook(invoice);
        this.cashBookService.saveCashBook(cashBook);
        return getMapperFactory().getInvoiceMapper().domainToResponse(invoice);

    }

}
