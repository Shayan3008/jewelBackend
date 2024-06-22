package com.jewelbackend.backend.setup.controllers;

import java.text.ParseException;
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
import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.GoldPurchaseRequestDto;
import com.jewelbackend.backend.setup.dto.response.GoldPurchaseResponseDto;
import com.jewelbackend.backend.setup.services.GoldPurchaseService;

@RestController
@RequestMapping("/goldpurchase")
public class GoldPurchaseController {
    private GoldPurchaseService goldPurchaseService;

    public GoldPurchaseController(GoldPurchaseService goldPurchaseService) {
        this.goldPurchaseService = goldPurchaseService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<GoldPurchaseResponseDto>>> getAllCurrenys(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String filter) throws ParseException {
        List<GoldPurchaseResponseDto> goldPurchaseResponseDTOs = this.goldPurchaseService
                .getAllGoldPurchases(size, page,HelperUtils.listToMap(filter));
        return ResponseEntity.ok()
                .body(new CommonResponse<>("All GoldPurchases", 200, goldPurchaseResponseDTOs,
                        this.goldPurchaseService.getDaoFactory().getGoldPurchaseDao().count()));
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<GoldPurchaseResponseDto>> saveCurrency(
            @RequestBody GoldPurchaseRequestDto goldPurchaseRequestDto) throws NotPresentException {
        GoldPurchaseResponseDto goldPurchaseResponseDto = this.goldPurchaseService
                .saveGoldPurchase(goldPurchaseRequestDto);
        return ResponseEntity.ok()
                .body(new CommonResponse<>("Saved Gold Transaction ", 200, goldPurchaseResponseDto, 1));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Integer>> deleteGoldPurchase(@PathVariable("id") int id)
            throws NotPresentException {
        this.goldPurchaseService.deleteGoldPurchase(id);
        return ResponseEntity.ok().body(new CommonResponse<>("deleted gold purchase ", 200, id, 1));
    }
}
