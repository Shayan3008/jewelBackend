package com.jewelbackend.backend.common.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.dto.request.ItemRequestDTO;

@Component
public class ItemValidator {
    public String validateItem(ItemRequestDTO item) {
        if (Objects.isNull(item.getItemName()) || item.getItemName().isEmpty())
            return "Karigar Name cannot be null";
        if (Objects.isNull(item.getNetWeight()) || item.getNetWeight() <= 0)
            return "Item net weight cannot be equal to or less than 0";

        if (Objects.isNull(item.getKarigarId()))
            return "Item must belong to a karigar";

        if (Objects.isNull(item.getCategoryId()))
            return "Item must belong to a category";

        if (Objects.isNull(item.getMetalName()))
            return "Item must belong to a metal type";

        return null;
    }

}
