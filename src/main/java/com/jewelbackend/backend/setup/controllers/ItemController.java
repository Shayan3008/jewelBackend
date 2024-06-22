package com.jewelbackend.backend.setup.controllers;

import java.text.ParseException;
import java.util.List;

import com.jewelbackend.backend.common.config.HelperUtils;
import com.jewelbackend.backend.common.criteriafilters.CriteriaFilter;
import com.jewelbackend.backend.setup.models.Item;
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
import com.jewelbackend.backend.common.exceptions.InvalidInputException;
import com.jewelbackend.backend.common.exceptions.NotPresentException;
import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.services.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
    ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("")
    ResponseEntity<CommonResponse<List<ItemResponseDTO>>> getAllItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String search) throws ParseException {
        List<ItemResponseDTO> itemResponseDTOs = itemService.findAllItems(page, size,search);
        if(!search.isBlank()) {
            CriteriaFilter<Item> criteriaFilter = new CriteriaFilter<>();
            Long count = criteriaFilter.getQueryCount(Item.class, HelperUtils.listToMap(search), itemService.getEntityManager());
            return ResponseEntity.ok().body(new CommonResponse<>("All items", HttpStatus.OK.value(), itemResponseDTOs, count));
        }
        CommonResponse<List<ItemResponseDTO>> commonResponse = new CommonResponse<>("All items", HttpStatus.OK.value(),
                itemResponseDTOs, this.itemService.getDaoFactory().getItemDao().count());
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping("/{id}")
    ResponseEntity<CommonResponse<ItemResponseDTO>> getItemById(@PathVariable("id") String id)
            throws NotPresentException {
        ItemResponseDTO item = this.itemService.findItemById(id);
        return ResponseEntity.ok().body(new CommonResponse<ItemResponseDTO>("id", 200, item));
    }

    @GetMapping("/findbycategory/{categoryid}")
    ResponseEntity<CommonResponse<List<ItemResponseDTO>>> getItemByCategoryId(@PathVariable("categoryid") String categoryId)
            throws NotPresentException {
        List<ItemResponseDTO> item = this.itemService.findItemByCategoryCode(Integer.parseInt(categoryId));
        return ResponseEntity.ok().body(new CommonResponse<>("id", 200, item));
    }

    @PostMapping("/save")
    ResponseEntity<CommonResponse<ItemResponseDTO>> saveItem(@RequestBody ItemRequestDTO itemRequestDTO)
            throws InvalidInputException {
        ItemResponseDTO itemResponseDTO = this.itemService.saveItem(itemRequestDTO);
        return ResponseEntity.status(200)
                .body(new CommonResponse<>("Save item", HttpStatus.OK.value(), itemResponseDTO));
    }

    @PutMapping("/update")
    ResponseEntity<CommonResponse<ItemResponseDTO>> updateItem(@RequestBody ItemRequestDTO itemRequestDTO)
            throws NotPresentException {

        ItemResponseDTO itemResponseDTO = this.itemService.updateItem(itemRequestDTO);
        return ResponseEntity.ok().body(new CommonResponse<>("Item Updated", HttpStatus.OK.value(), itemResponseDTO));

    }

    @DeleteMapping("/delete/{id}")
    ResponseEntity<CommonResponse<Integer>> deleteItem(@PathVariable("id") String id) throws NotPresentException {

        this.itemService.deleteItem(id);

        return ResponseEntity.ok()
                .body(new CommonResponse<>("Item Deleted", HttpStatus.OK.value(), Integer.parseInt(id)));

    }
}
