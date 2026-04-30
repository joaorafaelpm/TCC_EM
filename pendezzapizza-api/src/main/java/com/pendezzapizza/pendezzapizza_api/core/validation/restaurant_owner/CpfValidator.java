package com.pendezzapizza.pendezzapizza_api.core.validation.restaurant_owner;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<ValidCpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) return true; // deixa o @NotNull cuidar disso

        // remove mascara
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false; // 111.111.111-11 etc

        // primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++)
            sum += (cpf.charAt(i) - '0') * (10 - i);
        int first = 11 - (sum % 11);
        if (first >= 10) first = 0;
        if (first != (cpf.charAt(9) - '0')) return false;

        // segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++)
            sum += (cpf.charAt(i) - '0') * (11 - i);
        int second = 11 - (sum % 11);
        if (second >= 10) second = 0;
        return second == (cpf.charAt(10) - '0');
    }
}