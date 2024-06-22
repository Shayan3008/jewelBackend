package com.jewelbackend.backend.sale.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.sale.dto.request.CurrencyTransactionRequestDTO;
import com.jewelbackend.backend.sale.dto.response.CurrencyTransactionResponseDTO;
import com.jewelbackend.backend.sale.service.CurrencyTransactionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/currencytransaction")
public class CurrencyTransactionController {

    private final CurrencyTransactionService currencyTransactionService;

    public CurrencyTransactionController(CurrencyTransactionService currencyTransactionService) {
        this.currencyTransactionService = currencyTransactionService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<CurrencyTransactionResponseDTO>>> getAllCurrenyTransactions(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String search) throws ParseException {
        List<CurrencyTransactionResponseDTO> currencyTransactionResponseDTOs = this.currencyTransactionService
                .getAllCurrencyTransactions(size, page,search);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("All Currency transactions", 200, currencyTransactionResponseDTOs,
                        this.currencyTransactionService.getDaoFactory().getCurrencyTransactionDao().count()));
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<CurrencyTransactionResponseDTO>> saveCurrencyTransaction(
            @RequestBody CurrencyTransactionRequestDTO currencyTransactionRequestDTO) throws NotPresentException {
        CurrencyTransactionResponseDTO currencyTransactionResponseDTO = this.currencyTransactionService
                .saveCurrencyTransaction(currencyTransactionRequestDTO);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("Saved Currency Transaction", 200, currencyTransactionResponseDTO, 1));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Integer>> deleteCurrencyTransaction(@PathVariable("id") int id)
            throws AlreadyPresentException {
        this.currencyTransactionService.deleteCurrencyTransaction(id);
        return ResponseEntity.ok().body(new CommonResponse<>("deleted currency transaction", 200, id, 1));
    }

}
