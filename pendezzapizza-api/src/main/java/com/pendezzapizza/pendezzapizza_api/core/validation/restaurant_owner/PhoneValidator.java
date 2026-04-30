package com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) return true;

        phone = phone.replaceAll("[^0-9]", "");

        int len = phone.length();
        if (len != 10 && len != 11) return false;

        // celular com 11 dígitos deve começar com 9 após o DDD
        if (len == 11 && phone.charAt(2) != '9') return false;

        return true;
    }
}
 