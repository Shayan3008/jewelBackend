package com.jewelbackend.backend.common.validator;

import java.util.Objects;

import org.springframework.stereotype.Component;

import com.jewelbackend.backend.setup.models.Karigar;

@Component
public class KarigarValidator {
    public String validateKarigar(Karigar karigar) {
        if (Objects.isNull(karigar.getKarigarName()) || karigar.getKarigarName().isEmpty())
            return "Karigar Name cannot be null";
        return null;
    }

}
