package com.pendezzapizza.pendezzapizza_api.domain.model.enuns;

public enum SaleIncludeField {
    PRODUCTS,
    CUSTOMERS;

    public static SaleIncludeField fromString(String value) {
        return valueOf(value.trim().toUpperCase());
    }
}