package com.jewelbackend.backend.setup.controllers;

import java.text.ParseException;
import java.util.List;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.LedgerTransactionDto;
import com.jewelbackend.backend.setup.dto.response.KarigarResponseDTO;
import com.jewelbackend.backend.setup.dto.response.LedgerTransactionUpdateDto;
import com.jewelbackend.backend.setup.models.Karigar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.setup.dto.request.VendorRequestDTO;
import com.jewelbackend.backend.setup.models.Vendor;
import com.jewelbackend.backend.setup.services.VendorService;

@RestController
@RequestMapping("/vendor")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<VendorRequestDTO>>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) throws ParseException {

        if (!search.isBlank()) {
            CriteriaFilter<Vendor> criteriaFilter = new CriteriaFilter<>();
            long count = criteriaFilter.getQueryCount(Vendor.class, HelperUtils.listToMap(search), vendorService.getEntityManager());
            var resp = new CommonResponse<>("All Vendors Fetched",
                    HttpStatus.OK.value(), vendorService.getAllVendors(page, size,search), count);
            return ResponseEntity.status(200).body(resp);
        }

        var vendorResponse = new CommonResponse<>("All Vendors Fetched",
                HttpStatus.OK.value(), vendorService.getAllVendors(page, size,search),this.vendorService.getDaoFactory().getVendorDao().count());
        return ResponseEntity.status(200).body(vendorResponse);
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<Vendor>> saveKarigar(@RequestBody VendorRequestDTO vendorRequestDTO) {
        var message = vendorRequestDTO.getId() == null ? "Vendor Saved" : "Vendor Updated";
        CommonResponse<Vendor> karigarCommonResponse = new CommonResponse<>(message,
                HttpStatus.OK.value(), vendorService.saveVendor(vendorRequestDTO));
        return ResponseEntity.status(200).body(karigarCommonResponse);
    }

    @GetMapping("/getGoldTransactionForVendor/{id}")
    ResponseEntity<CommonResponse<LedgerTransactionUpdateDto>> getGoldTransactionForVendor(@PathVariable int id) throws NotPresentException {
        var ledgerTransactionDtoCommonResponse = new CommonResponse<>("Ledger Transaction Fetched",
                HttpStatus.OK.value(), vendorService.getGoldTransactionForVendor(id));
        return ResponseEntity.status(200).body(ledgerTransactionDtoCommonResponse);
    }

    @PostMapping("/convertgoldtoledger")
    ResponseEntity<CommonResponse<LedgerTransactionDto>> convertGoldToLedger(@RequestBody LedgerTransactionDto ledgerTransactionDto) throws NotPresentException {
        var ledgerTransaction = vendorService.saveLedger(ledgerTransactionDto);
        return ResponseEntity.status(200).body(new CommonResponse<>("Ledger Transaction Saved", HttpStatus.OK.value(), ledgerTransaction));
    }

    @GetMapping("/getvendorbyvendorheader/{id}")
    ResponseEntity<CommonResponse<List<VendorRequestDTO>>> getVendorByVendorHeader(@PathVariable int id) throws NotPresentException {
        var vendorResponse = new CommonResponse<>("All Vendors Fetched",
                HttpStatus.OK.value(), vendorService.getVendorByVendorHeader(id));
        return ResponseEntity.status(200).body(vendorResponse);
    }
}
