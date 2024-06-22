package com.jewelbackend.backend.setup.controllers;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.setup.dto.response.VendorHeaderResponseDTO;
import com.jewelbackend.backend.setup.models.VendorHeader;
import com.jewelbackend.backend.setup.services.VendorHeaderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/vendor-header")
public class VendorHeaderController {
    private final VendorHeaderService vendorHeaderService;

    public VendorHeaderController(VendorHeaderService vendorHeaderService) {
        this.vendorHeaderService = vendorHeaderService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<VendorHeaderResponseDTO>>> getAllVendorHeaders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) throws ParseException {

        if (!search.isBlank()) {
            CriteriaFilter<VendorHeader> criteriaFilter = new CriteriaFilter<>();
            long count = criteriaFilter.getQueryCount(VendorHeader.class, HelperUtils.listToMap(search), vendorHeaderService.getEntityManager());
            var resp = new CommonResponse<>("All Vendor Headers Fetched",
                    HttpStatus.OK.value(), vendorHeaderService.findAllVendorHeaders(page, size,search), count);
            return ResponseEntity.status(200).body(resp);
        }

        var vendorResponse = new CommonResponse<>("All Vendors Fetched",
                HttpStatus.OK.value(), vendorHeaderService.findAllVendorHeaders(page, size,search));
        return ResponseEntity.status(200).body(vendorResponse);
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<VendorHeaderResponseDTO>> saveVendorHeader(@RequestBody VendorHeader vendorHeader) {
        var message = vendorHeader.getId() == null ? "Vendor Header Saved" : "Vendor Header Updated";
        var vendorHeaderCommonResponse = new CommonResponse<>(message,
                HttpStatus.OK.value(), vendorHeaderService.saveVendorHeader(vendorHeader));
        return ResponseEntity.status(200).body(vendorHeaderCommonResponse);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CommonResponse<String>> deleteVendorHeader(@PathVariable int id) {
        vendorHeaderService.deleteVendorHeader(id);
        var vendorHeaderCommonResponse = new CommonResponse<>("Vendor Header Deleted",
                HttpStatus.OK.value(), "Vendor Header Deleted");
        return ResponseEntity.status(200).body(vendorHeaderCommonResponse);
    }
}
