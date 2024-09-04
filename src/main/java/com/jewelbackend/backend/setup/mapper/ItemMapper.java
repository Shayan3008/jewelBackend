package com.jewelbackend.backend.setup.mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import com.jewelbackend.backend.common.config.HelperUtils;
import org.apache.logging.log4j.Level;
import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;
import com.jewelbackend.backend.setup.dto.response.ItemResponseDTO;
import com.jewelbackend.backend.setup.models.Item;

@Component
public class ItemMapper {
    public ItemResponseDTO domainToResponse(Item item) throws IOException {
        ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
        itemResponseDTO.setId(item.getId());
        itemResponseDTO.setCategoryId(item.getCategory().getCategoryCode());
        itemResponseDTO.setDesignNo(item.getDesignNo());
        itemResponseDTO.setKarat(item.getKarat());
        itemResponseDTO.setMetalName(item.getCategory().getMetalType().getMetalName());
        itemResponseDTO.setKarigarId(item.getKarigar().getId());
        itemResponseDTO.setNetWeight(item.getNetWeight());

        if(item.getItemImagePath() != null){
            HelperUtils.logMessage(Level.ERROR,"Here is the path: "+item.getItemImagePath());
            Path path = Paths.get(item.getItemImagePath());
            HelperUtils.logMessage(Level.ERROR,"Here is the path: "+item.getItemImagePath());
            if(Files.exists(path)){

                var image = Files.readAllBytes(path);
                itemResponseDTO.setItemImage(
                        image == null ? "" : Base64.getEncoder().encodeToString(image));
            }else{
                HelperUtils.logMessage(Level.ERROR,"Here is the path: "+item.getItemImagePath());
            }
        }
        else{
            HelperUtils.logMessage(Level.ERROR,"Image path is null");
        }
        itemResponseDTO.setCategoryName(item.getCategory().getCategoryName());
        itemResponseDTO.setKarigarName(item.getKarigar().getKarigarName());
        itemResponseDTO.setDescription(item.getDescription());
        itemResponseDTO.setQty(item.getQty());
        itemResponseDTO.setBeedsWeight(item.getBeedsWeight());
        itemResponseDTO.setBigStoneWeight(item.getBigStoneWeight());
        itemResponseDTO.setSmallStoneQty(item.getSmallStoneQty());
        itemResponseDTO.setDiamondQty(item.getDiamondQty());
        itemResponseDTO.setDiamondWeight(item.getDiamondWeight());
        itemResponseDTO.setMultiItem(item.getMultiItem());
        itemResponseDTO.setTotalQty(item.getTotalQty());
        itemResponseDTO.setTotalMultiWeight(item.getTotalMultiWeight());
        itemResponseDTO.setRemainingNetWeight(item.getRemainingNetWeight());
        return itemResponseDTO;
    }

    public Item requestToDomain(ItemRequestDTO itemRequestDTO) {
        Item item = new Item();
        item.setId(itemRequestDTO.getId());
        item.setDesignNo(itemRequestDTO.getDesignNo());
        item.setKarat(itemRequestDTO.getKarat());

        item.setNetWeight(itemRequestDTO.getNetWeight());
        item.setItemImage(
                itemRequestDTO.getItemImage() == null ? null
                        : Base64.getDecoder().decode(itemRequestDTO.getItemImage()));
        item.setDescription(itemRequestDTO.getDescription());
        item.setQty(itemRequestDTO.getQty());
        item.setBeedsWeight(itemRequestDTO.getBeedsWeight());
        item.setBigStoneWeight(itemRequestDTO.getBigStoneWeight());
        item.setSmallStoneQty(itemRequestDTO.getSmallStoneQty());
        item.setDiamondQty(itemRequestDTO.getDiamondQty());
        item.setDiamondWeight(itemRequestDTO.getDiamondWeight());
        item.setTotalQty(itemRequestDTO.getQty());
        item.setTotalMultiWeight(itemRequestDTO.getNetWeight());
        item.setRemainingNetWeight(itemRequestDTO.getNetWeight());

        return item;
    }


}
