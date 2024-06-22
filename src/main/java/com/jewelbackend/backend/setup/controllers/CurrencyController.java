package com.jewelbackend.backend.setup.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.CurrencyRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CurrencyResponseDTO;
import com.jewelbackend.backend.setup.services.CurrencyService;

@RestController
@RequestMapping("/currency")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<CurrencyResponseDTO>>> getAllCurrenys(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        List<CurrencyResponseDTO> currencyResponseDTOs = this.currencyService
                .getAllCurrency(size, page);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("All Currency s", 200, currencyResponseDTOs,
                        this.currencyService.getDaoFactory().getCurrencyDao().count()));
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<CurrencyResponseDTO>> saveCurrency(
            @RequestBody CurrencyRequestDTO currencyRequestDTO){
        CurrencyResponseDTO currencyResponseDTO = this.currencyService
                .saveCurrency(currencyRequestDTO);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("Saved Currency ", 200, currencyResponseDTO, 1));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Integer>> deleteCurrency(@PathVariable("id") int id)
            throws NotPresentException {
        this.currencyService.deleteCurrency(id);
        return ResponseEntity.ok().body(new CommonResponse<>("deleted currency ", 200, id, 1));
    }
}
