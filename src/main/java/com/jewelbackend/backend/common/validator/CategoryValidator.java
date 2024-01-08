package com.jewelbackend.backend.common.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.models.Category;

@Component
public class CategoryValidator {
    public String validateCategory(Category category) {
        if (Objects.isNull(category.getCategoryName()))
            return "Category Name cannot be null";
        return null;
    }
}
