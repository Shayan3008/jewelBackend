package com.jewelbackend.backend.setup.mapper;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.setup.dto.request.InvoiceRequestDTO;
import com.jewelbackend.backend.setup.dto.response.InvoiceResponseDto;
import com.jewelbackend.backend.setup.models.CashBook;
import com.jewelbackend.backend.setup.models.Invoice;

import lombok.Data;

@Component
@Data
public class InvoiceMapper {
    public Invoice requestToDomain(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceRequestDTO.getId());
        invoice.setQty(invoiceRequestDTO.getQty());
        if (invoiceRequestDTO.getInvoiceDate() == null) {
            invoice.setInvoiceDate(new Date());
        } else {
            invoice.setInvoiceDate(invoiceRequestDTO.getInvoiceDate());
        }
        invoice.setTotalWeight(invoiceRequestDTO.getTotalWeight());
        invoice.setWastePer(invoiceRequestDTO.getWastePer());
        invoice.setMaking(invoiceRequestDTO.getMaking());
        invoice.setTotalItemPrice(invoiceRequestDTO.getTotalItemPrice());
        invoice.setTotalBill(invoiceRequestDTO.getTotalBill());
        invoice.setBeadAmount(invoiceRequestDTO.getBeadAmount());
        invoice.setBigStoneAmount(invoiceRequestDTO.getBigStoneAmount());
        invoice.setSmallStoneAmount(invoiceRequestDTO.getSmallStoneAmount());
        invoice.setDoliPolish(invoiceRequestDTO.getDoliPolish());
        invoice.setDiamondAmount(invoiceRequestDTO.getDiamondAmount());
        invoice.setChandiAmount(invoiceRequestDTO.getChandiAmount());
        invoice.setDiscount(invoiceRequestDTO.getDiscount());
        invoice.setDescription(invoiceRequestDTO.getDescription());
        invoice.setDiamondRate(invoiceRequestDTO.getDiamondRate());
        return invoice;
    }

    public InvoiceResponseDto domainToResponse(Invoice invoice) {
        InvoiceResponseDto invoiceResponseDto = new InvoiceResponseDto();
        invoiceResponseDto.setId(invoice.getId());
        if (invoice.getItem() != null)
            invoiceResponseDto.setCategoryName(invoice.getItem().getCategory().getCategoryName());
        invoiceResponseDto.setQty(invoice.getQty());
        invoiceResponseDto.setTotalWeight(invoice.getTotalWeight());
        invoiceResponseDto.setWastePer(invoice.getWastePer());
        invoiceResponseDto.setMaking(invoice.getMaking());
        invoiceResponseDto.setTotalItemPrice(invoice.getTotalItemPrice());
        invoiceResponseDto.setTotalBill(invoice.getTotalBill());
        invoiceResponseDto.setInvoiceDate(invoice.getInvoiceDate());
        invoiceResponseDto.setBeadAmount(invoice.getBeadAmount());
        invoiceResponseDto.setBigStoneAmount(invoice.getBigStoneAmount());
        invoiceResponseDto.setSmallStoneAmount(invoice.getSmallStoneAmount());
        invoiceResponseDto.setDoliPolish(invoice.getDoliPolish());
        invoiceResponseDto.setDiamondAmount(invoice.getDiamondAmount());
        invoiceResponseDto.setChandiAmount(invoice.getChandiAmount());
        invoiceResponseDto.setDiscount(invoice.getDiscount());
        invoiceResponseDto.setDescription(invoice.getDescription());
        invoiceResponseDto.setDiamondRate(invoice.getDiamondRate());
        return invoiceResponseDto;
    }

    public CashBook invoiceToCashBook(Invoice invoice) {
        CashBook cashBook = new CashBook();
        cashBook.setAmount(invoice.getTotalBill());
        cashBook.setTrnDate(invoice.getInvoiceDate());
        cashBook.setTrnType(Constants.SALE_CASH);
        return cashBook;
    }

}
