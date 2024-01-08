package com.jewelbackend.backend.setup.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.KarigarRequestDTO;
import com.jewelbackend.backend.setup.dto.response.KarigarResponseDTO;
import com.jewelbackend.backend.setup.services.KarigarService;

@RestController
@RequestMapping("/karigar")
public class KarigarController {

    final KarigarService karigarService;

    KarigarController(KarigarService karigarService) {
        this.karigarService = karigarService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<KarigarResponseDTO>>> getAllKarigars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<KarigarResponseDTO> karigarResponseDTOs = karigarService.getAllKarigars(page, size);
        CommonResponse<List<KarigarResponseDTO>> karigarResponseDTOsResponse = new CommonResponse<>("All Karigars",
                HttpStatus.OK.value(), karigarResponseDTOs);
        return ResponseEntity.status(200).body(karigarResponseDTOsResponse);
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<KarigarResponseDTO>> saveKarigar(@RequestBody KarigarRequestDTO karigarRequestDTO)
            throws InvalidInputException, AlreadyPresentException {
        KarigarResponseDTO karigarResponseDTO = karigarService.saveKarigar(karigarRequestDTO);
        CommonResponse<KarigarResponseDTO> karigarCommonResponse = new CommonResponse<>("Karigar Saved",
                HttpStatus.OK.value(), karigarResponseDTO);
        return ResponseEntity.status(200).body(karigarCommonResponse);
    }

    @PutMapping("/update")
    ResponseEntity<CommonResponse<KarigarResponseDTO>> updateKarigar(@RequestBody KarigarRequestDTO karigarRequestDTO)
            throws NotPresentException, InvalidInputException {

        KarigarResponseDTO karigarResponseDTO = karigarService.updateKarigar(karigarRequestDTO);
        CommonResponse<KarigarResponseDTO> karigarCommonResponse = new CommonResponse<>("Karigar Saved",
                HttpStatus.OK.value(), karigarResponseDTO);
        return ResponseEntity.status(200).body(karigarCommonResponse);

    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CommonResponse<Integer>> deleteKarigar(@PathVariable("id") String id) {
        try {

            this.karigarService.deleteKarigar(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonResponse<Integer> commonResponse = new CommonResponse<>("Karigar deleted", HttpStatus.OK.value(),
                Integer.parseInt(id));
        return ResponseEntity.status(200).body(commonResponse);
    }
}
