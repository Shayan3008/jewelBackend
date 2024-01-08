package com.jewelbackend.backend.setup.mapper;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.models.Item;

@Component
public class ItemMapper {
    public ItemResponseDTO domainToResponse(Item item) {
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setId(item.getId());
        itemResponseDTO.setItemName(item.getItemName());
        itemResponseDTO.setCategoryId(item.getCategory().getCategoryCode());
        itemResponseDTO.setDesignNo(item.getDesignNo());
        itemResponseDTO.setKarat(item.getKarat());
        itemResponseDTO.setMetalName(item.getMetalType().getMetalName());
        itemResponseDTO.setKarigarId(item.getKarigar().getId());
        itemResponseDTO.setNetWeight(item.getNetWeight());

        itemResponseDTO.setItemImage(
                item.getItemImage() == null ? "" : Base64.getEncoder().encodeToString(item.getItemImage()));
        itemResponseDTO.setCategoryName(item.getCategory().getCategoryName());
        itemResponseDTO.setKarigarName(item.getKarigar().getKarigarName());
        return itemResponseDTO;
    }

    public Item requestToDomain(ItemRequestDTO itemRequestDTO) {
        Item item = new Item();
        item.setId(itemRequestDTO.getId());
        item.setItemName(itemRequestDTO.getItemName());
        item.setDesignNo(itemRequestDTO.getDesignNo());
        item.setKarat(itemRequestDTO.getKarat());

        item.setNetWeight(itemRequestDTO.getNetWeight());
        item.setItemImage(
                itemRequestDTO.getItemImage() == null ? null
                        : Base64.getDecoder().decode(itemRequestDTO.getItemImage()));
        return item;
    }

    public ItemResponseDTO requestToResponse(ItemRequestDTO itemRequestDTO) {
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setId(itemRequestDTO.getId());
        itemResponseDTO.setItemName(itemRequestDTO.getItemName());
        itemResponseDTO.setCategoryId(itemRequestDTO.getCategoryId());
        itemResponseDTO.setDesignNo(itemRequestDTO.getDesignNo());
        itemResponseDTO.setKarat(itemRequestDTO.getKarat());
        itemResponseDTO.setMetalName(itemRequestDTO.getMetalName());
        itemResponseDTO.setKarigarId(itemRequestDTO.getKarigarId());
        itemResponseDTO.setNetWeight(itemRequestDTO.getNetWeight());

        return itemResponseDTO;
    }
}
