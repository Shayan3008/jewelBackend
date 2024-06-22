package com.jewelbackend.backend.setup.controllers;

import com.jewelbackend.backend.setup.services.CashBookViewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cashbookview")
public class CashBookViewController {
    private final CashBookViewService cashBookViewService;

    public CashBookViewController(CashBookViewService cashBookViewService) {
        this.cashBookViewService = cashBookViewService;
    }

    @GetMapping("")
    public String createCashBookFromView() {
        return cashBookViewService.createCashBookFromView();
    }
}
