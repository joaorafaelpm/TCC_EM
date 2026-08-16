package com.pendezzapizza.pendezzapizza_api.core.validation;

import jakarta.validation.ConstraintValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<ValidCep, String> {

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null) return true;

        String unmaskedCep = cep.replaceAll("[^0-9]", "");

        if (unmaskedCep.length() != 8) {
            return false;
        }

        if (unmaskedCep.matches("(\\d)\\1{7}")) {
            return false; // 00000000, 11111111, etc.
        }

        return true;
    }
}