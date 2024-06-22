package com.jewelbackend.backend.reports.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import com.jewelbackend.backend.common.config.ResponseReport;
import com.jewelbackend.backend.reports.services.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.exceptions.EmptyReportException;
import com.jewelbackend.backend.reports.services.invoicereport.InvoiceReportService;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final InvoiceReportService invoiceReportService;

    private final CurrencyReportService currencyReportService;
    private final CashBookReportService cashBookReportService;

    private final LedgerTransactionReportService ledgerTransactionReportService;

    private final CategoryReportService categoryReportService;

    private final DetailedCategoryReportService detailedCategoryReportService;

    private final  TrailBalanceReportService trailBalanceReportService;

    public ReportController(InvoiceReportService invoiceReportService,
                            CurrencyReportService currencyReportService, CashBookReportService cashBookReportService,
                            LedgerTransactionReportService ledgerTransactionReportService, CategoryReportService categoryReportService,
                            DetailedCategoryReportService detailedCategoryReportService, TrailBalanceReportService trailBalanceReportService) {
        this.invoiceReportService = invoiceReportService;
        this.currencyReportService = currencyReportService;
        this.cashBookReportService = cashBookReportService;
        this.ledgerTransactionReportService = ledgerTransactionReportService;
        this.categoryReportService = categoryReportService;
        this.detailedCategoryReportService = detailedCategoryReportService;
        this.trailBalanceReportService = trailBalanceReportService;
    }

    @GetMapping("/invoicereport/{id}")
    public ResponseEntity<ResponseReport<byte[]>> generateInvoiceReport(@PathVariable String id) throws EmptyReportException, IOException, ParseException {

        var bytes = this.invoiceReportService.generateReport(Map.of("invoiceId", id), "PDF", "Invoice Report");
        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Ledger_Transaction", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", bytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cashbookreport")
    public ResponseEntity<ResponseReport<byte[]>> generateCashBookReport(
            @RequestParam(defaultValue = "") String filter) throws EmptyReportException, IOException, ParseException {

        byte[] reportBytes = this.cashBookReportService.generateReport(HelperUtils.listToMap(filter), "xlsx",
                "CashBook Transactions");
        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("CashBook_Transactions", "xlsx");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cashbookreportpdf")
    public ResponseEntity<ResponseReport<byte[]>> generateCashBookReportPDF(
            @RequestParam(defaultValue = "") String filter) throws EmptyReportException, IOException, ParseException {

        byte[] reportBytes = this.cashBookReportService.generateReport(HelperUtils.listToMap(filter), "pdf",
                "CashBook Transactions");

        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Ledger_Transaction", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }

    @GetMapping("/currencyreportpdf")
    public ResponseEntity<ResponseReport<byte[]>> generateCurrencyTransactionReportPDF(
            @RequestParam(defaultValue = "") String filter) throws ParseException, EmptyReportException, IOException {

        byte[] reportBytes = this.ledgerTransactionReportService.generateReport(HelperUtils.listToMap(filter), "PDF",
                "Ledger Transactions");

        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Ledger_Transaction", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/detailedcurrencyreportpdf")
    public ResponseEntity<ResponseReport<byte[]>> generateDetailedCurrencyTransactionReportPDF(
            @RequestParam(defaultValue = "") String filter) throws ParseException, EmptyReportException, IOException {

        byte[] reportBytes = this.currencyReportService.generateReport(HelperUtils.listToMap(filter), "PDF",
                "Currency Transactions");
        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Report_Name", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/categorycompletereport")
    public ResponseEntity<ResponseReport<byte[]>> generateCategoryCompleteReport(
            @RequestParam(defaultValue = "") String filter) throws ParseException, EmptyReportException, IOException {

        byte[] reportBytes = this.categoryReportService.generateReport(HelperUtils.listToMap(filter), "PDF",
                "Category Complete Report");

        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Category_Complete_Report", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/detailedcategoryreport/{id}")
    public ResponseEntity<ResponseReport<byte[]>> generateDetailedCategoryReport(@PathVariable("id") Integer categoryId) throws EmptyReportException, IOException, ParseException {
        Map<String,String> map = Map.of("categoryId", categoryId.toString());
        var reportBytes = this.detailedCategoryReportService.generateReport(map, "PDF", "Detailed Category Report");
        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Detailed_category_report_" + categoryId.toString(), "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/vendortrailbalancereport")
    public ResponseEntity<ResponseReport<byte[]>> generateVendorTrailBalanceReport(
            @RequestParam(defaultValue = "") String filter) throws ParseException, EmptyReportException, IOException {

        byte[] reportBytes = this.trailBalanceReportService.generateReport(HelperUtils.listToMap(filter), "PDF",
                "Vendor Trail Balance Report");

        HttpHeaders httpHeaders = HelperUtils.generateHeadersForReport("Vendor_Trail_Balance_Report", "pdf");
        ResponseReport<byte[]> response = new ResponseReport<byte[]>("Success", "Report Generated", "0000", reportBytes, httpHeaders);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
