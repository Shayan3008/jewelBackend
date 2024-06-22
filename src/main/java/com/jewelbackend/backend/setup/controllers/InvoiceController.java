package com.jewelbackend.backend.setup.controllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.InvoiceRequestDTO;
import com.jewelbackend.backend.setup.dto.response.InvoiceResponseDto;
import com.jewelbackend.backend.setup.services.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {
    final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("")
    public ResponseEntity<CommonResponse<List<InvoiceResponseDto>>> findAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) {
        List<InvoiceResponseDto> invoiceResponseDtos = this.invoiceService.findAllInvoices(page, size,search);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("All Invoices", 200, invoiceResponseDtos,
                        invoiceService.getDaoFactory().getInvoiceDao().count()));
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<InvoiceResponseDto>> saveInvoice(
            @RequestBody InvoiceRequestDTO invoiceRequestDTO)
            throws InvalidInputException {
        InvoiceResponseDto invoiceResponseDto = this.invoiceService.saveInvoice(invoiceRequestDTO);
        return ResponseEntity.ok().body(new CommonResponse<>("Saved invoice", 200, invoiceResponseDto));
    }

    @PutMapping
    public ResponseEntity<CommonResponse<InvoiceResponseDto>> updateInvoice(
            @RequestBody InvoiceRequestDTO invoiceRequestDTO) throws InvalidInputException, NotPresentException {
        InvoiceResponseDto invoiceResponseDto = this.invoiceService.updateInvoice(invoiceRequestDTO);
        return ResponseEntity.ok().body(new CommonResponse<>(
                "Invoice Updated",
                200,
                invoiceResponseDto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Integer>> deleteInvoice(
            @PathVariable("id") String id) throws NotPresentException {
        this.invoiceService.deleteInvoice(id);
        return ResponseEntity.ok().body(new CommonResponse<>("Invoice deleted", 200, Integer.parseInt(id)));
    }

    @GetMapping("/getgoldrate")
    public ResponseEntity<CommonResponse<BigDecimal>> getGoldRate(
            @RequestParam("date") String date) throws NotPresentException {
        BigDecimal price = this.invoiceService.getGoldRate(date);
        return ResponseEntity.ok().body(new CommonResponse<>("Gold Rate from " + date, 200, price));
    }

    @PostMapping("/savewithoutitem")
    public ResponseEntity<CommonResponse<InvoiceResponseDto>> saveInvoiceWithoutItem(
            @RequestBody InvoiceRequestDTO invoiceRequestDTO) {
        InvoiceResponseDto invoiceResponseDto = this.invoiceService.saveInvoiceWithoutItem(invoiceRequestDTO);
        return ResponseEntity.ok().body(new CommonResponse<>("Saved invoice", 200, invoiceResponseDto));
    }
}
