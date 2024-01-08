package com.jewelbackend.backend.setup.mapper;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.InvoiceRequestDTO;
import com.jewelbackend.backend.setup.dto.response.InvoiceResponseDto;
import com.jewelbackend.backend.setup.models.Invoice;

import lombok.Data;

@Component
@Data
public class InvoiceMapper {
    public Invoice requestToDomain(InvoiceRequestDTO invoiceRequestDTO) {
        Invoice invoice = new Invoice();
        invoice.setId(invoiceRequestDTO.getId());
        invoice.setQty(invoiceRequestDTO.getQty());
        invoice.setInvoiceDate(invoiceRequestDTO.getInvoiceDate());
        invoice.setTotalWeight(invoiceRequestDTO.getTotalWeight());
        invoice.setWastePer(invoiceRequestDTO.getWastePer());
        invoice.setMaking(invoiceRequestDTO.getMaking());
        invoice.setTotalItemPrice(invoiceRequestDTO.getTotalItemPrice());
        invoice.setTotalBill(invoiceRequestDTO.getTotalBill());
        return invoice;
    }

    public InvoiceResponseDto domainToResponse(Invoice invoice) {
        InvoiceResponseDto invoiceResponseDto = new InvoiceResponseDto();
        invoiceResponseDto.setId(invoice.getId());
        invoiceResponseDto.setItemName(invoice.getItem().getItemName());
        invoiceResponseDto.setQty(invoice.getQty());
        invoiceResponseDto.setTotalWeight(invoice.getTotalWeight());
        invoiceResponseDto.setWastePer(invoice.getWastePer());
        invoiceResponseDto.setMaking(invoice.getMaking());
        invoiceResponseDto.setTotalItemPrice(invoice.getTotalItemPrice());
        invoiceResponseDto.setTotalBill(invoice.getTotalBill());
        invoiceResponseDto.setInvoiceDate(invoice.getInvoiceDate());
        return invoiceResponseDto;
    }
}
