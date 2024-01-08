package com.jewelbackend.backend.setup.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import com.jewelbackend.backend.auth.JwtAuthConfig;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.common.validator.ValidatorFactory;
import com.jewelbackend.backend.setup.dao.DaoFactory;
import com.jewelbackend.backend.setup.dto.request.InvoiceRequestDTO;
import com.jewelbackend.backend.setup.dto.response.InvoiceResponseDto;
import com.jewelbackend.backend.setup.mapper.MapperFactory;
import com.jewelbackend.backend.setup.models.Invoice;
import com.jewelbackend.backend.setup.models.Item;

@Service
public class InvoiceService extends BaseService {

    public InvoiceService(DaoFactory daoFactory, ValidatorFactory validatorFactory, MapperFactory mapperFactory,
            AuthenticationManager authenticationManager, JwtAuthConfig jwtAuthConfig) {
        super(daoFactory, validatorFactory, mapperFactory, authenticationManager, jwtAuthConfig);
    }

    public List<InvoiceResponseDto> findAllInvoices(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Invoice> invoicPage = getDaoFactory().getInvoiceDao().findAll(pageRequest);
        List<Invoice> invoices = invoicPage.getContent();
        return invoices.stream().map(e -> getMapperFactory().getInvoiceMapper().domainToResponse(e))
                .collect(Collectors.toList());
    }

    public InvoiceResponseDto saveInvoice(InvoiceRequestDTO invoiceRequestDTO) throws InvalidInputException {
        Invoice invoice = getMapperFactory().getInvoiceMapper().requestToDomain(invoiceRequestDTO);
        Optional<Item> item = getDaoFactory().getItemDao().findById(invoiceRequestDTO.getItemId());
        if (item.isEmpty())
            throw new InvalidInputException("Item with id not found");
        invoice.setItem(item.get());
        invoice = getDaoFactory().getInvoiceDao().save(invoice);
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

}
