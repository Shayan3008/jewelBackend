package com.jewelbackend.backend.setup.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.setup.dto.response.MetalTypeResponseDTO;
import com.jewelbackend.backend.setup.services.MetalTypeService;

@RestController
@RequestMapping("/metalType")
public class MetalTypeController {

    MetalTypeService metalTypeService;

    MetalTypeController(MetalTypeService metalTypeService){
        this.metalTypeService = metalTypeService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<MetalTypeResponseDTO>>> getAllMetalTypes() {
        List<MetalTypeResponseDTO> metalTypeResponseDTOs = metalTypeService.getAllMetalTypes();
        CommonResponse<List<MetalTypeResponseDTO>> commonResponse = new CommonResponse<>("All metal types",
                HttpStatus.OK.value(), metalTypeResponseDTOs);
        return ResponseEntity.status(200).body(commonResponse);
    }
}
