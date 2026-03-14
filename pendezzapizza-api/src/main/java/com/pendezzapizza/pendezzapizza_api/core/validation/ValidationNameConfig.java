package com.pendezzapizza.pendezzapizza_api.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;
public class ValidationNameConfig implements ConstraintValidator<ValidationName, String> {

    private int min;
    private int max;
    private boolean allowHyphen;
    private boolean allowApostrophe;

    private Pattern pattern;

    @Override
    public void initialize(ValidationName annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
        this.allowHyphen = annotation.allowHyphen();
        this.allowApostrophe = annotation.allowApostrophe();

        // Monta dinamicamente a regex permitida
        StringBuilder regex = new StringBuilder("^[A-Za-zÀ-ÖØ-öø-ÿ ");

        if (allowHyphen) regex.append("\\-");
        if (allowApostrophe) regex.append("'");

        regex.append("]+$");

        pattern = Pattern.compile(regex.toString());


    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;

        String cleaned = value.trim();

        // tamanho válido
        if (cleaned.length() < min || cleaned.length() > max)
            return false;

        // não permitir string vazia após trim
        if (cleaned.isEmpty())
            return false;

        // bloquear placeholders tipo {nome}
        if (cleaned.matches("\\{.*}"))
            return false;

        // validar apenas caracteres permitidos
        return pattern.matcher(cleaned).matches();
    }
}