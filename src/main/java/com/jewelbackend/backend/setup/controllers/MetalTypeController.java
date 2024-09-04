package com.jewelbackend.backend.setup.controllers;

import java.text.ParseException;
import java.util.List;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.common.exceptions.AlreadyPresentException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.MetalTypeRequestDTO;
import com.jewelbackend.backend.setup.dto.response.CategoryResponseDTO;
import com.jewelbackend.backend.setup.models.Item;
import com.jewelbackend.backend.setup.models.MetalType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jewelbackend.backend.common.config.CommonResponse;
import com.jewelbackend.backend.setup.dto.response.MetalTypeResponseDTO;
import com.jewelbackend.backend.setup.services.MetalTypeService;

@RestController
@RequestMapping("/metalType")
public class MetalTypeController {

    MetalTypeService metalTypeService;

    MetalTypeController(MetalTypeService metalTypeService) {
        this.metalTypeService = metalTypeService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<MetalTypeResponseDTO>>> getAllMetalTypes() {
        List<MetalTypeResponseDTO> metalTypeResponseDTOs = metalTypeService.getAllMetalTypes();
        CommonResponse<List<MetalTypeResponseDTO>> commonResponse = new CommonResponse<>("All metal types",
                HttpStatus.OK.value(), metalTypeResponseDTOs);
        return ResponseEntity.status(200).body(commonResponse);
    }

    @GetMapping("/list")
    ResponseEntity<CommonResponse<List<MetalTypeResponseDTO>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) throws ParseException {
        List<MetalTypeResponseDTO> metalTypeResponseDTOS = metalTypeService.getAllMetalTypes(page, size, search);
        if (!search.isBlank()) {
            CriteriaFilter<MetalType> criteriaFilter = new CriteriaFilter<>();
            long count = criteriaFilter.getQueryCount(MetalType.class, HelperUtils.listToMap(search), metalTypeService.getEntityManager());
            return ResponseEntity.ok().body(new CommonResponse<>("All items", HttpStatus.OK.value(), metalTypeResponseDTOS, count));
        }
        CommonResponse<List<MetalTypeResponseDTO>> metalTypeResponseDTOs = new CommonResponse<>("List All Categories",
                HttpStatus.OK.value(), metalTypeResponseDTOS,
                this.metalTypeService.getDaoFactory().getMetalTypeDao().count());
        return ResponseEntity.status(200).body(metalTypeResponseDTOs);
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<String>> saveMetalType(@RequestBody MetalTypeRequestDTO metalTypeRequestDTO) throws AlreadyPresentException {
        metalTypeService.saveMetalType(metalTypeRequestDTO);
        CommonResponse<String> commonResponse = new CommonResponse<>("Metal type saved", HttpStatus.OK.value(), "Metal type saved");
        return ResponseEntity.status(200).body(commonResponse);
    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CommonResponse<String>> deleteMetalType(@PathVariable String id) throws NotPresentException {
        metalTypeService.deleteMetalType(id);
        CommonResponse<String> commonResponse = new CommonResponse<>("Metal type deleted", HttpStatus.OK.value(), "Metal type deleted");
        return ResponseEntity.status(200).body(commonResponse);
    }
}
