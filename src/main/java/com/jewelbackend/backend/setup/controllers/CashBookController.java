package com.jewelbackend.backend.setup.controllers;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.jewelbackend.backend.common.constants.Constants;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.setup.models.CashBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.setup.dto.response.CashBookResponseDTO;
import com.jewelbackend.backend.setup.services.CashBookService;

@RestController
@RequestMapping("/cashbook")
public class CashBookController {

    final CashBookService cashBookService;

    public CashBookController(CashBookService cashBookService) {
        this.cashBookService = cashBookService;
    }

    // Filter Api: Will be seperated like this trnType,
    @GetMapping("")
    public ResponseEntity<CommonResponse<List<CashBookResponseDTO>>> getAllCashBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String filter) throws ParseException {
        List<CashBookResponseDTO> cashBooks = cashBookService.getAllCashBookByTransactionType(filter, page, size);
        CommonResponse<List<CashBookResponseDTO>> commonResponse = new CommonResponse<>(filter, 200, cashBooks,
                cashBookService.getDaoFactory().getCashBookDao().count());
        return ResponseEntity.ok().body(commonResponse);
    }

    @PostMapping("/addOpeningBalance")
    public ResponseEntity<CommonResponse<String>> addOpeningBalance(@RequestBody CashBook cashBook) throws AlreadyPresentException {
        cashBook.setTrnType(Constants.PURCHASE_CASH);
//        cashBook.setAmount(BigDecimal.ZERO);
        cashBookService.addOpeningBalanceForDate(cashBook);
        var commonResponse = new CommonResponse<>("Opening Balance Added", 200, "Opening Balance Added Successfully", 1);
        return ResponseEntity.ok().body(commonResponse);
    }

}
