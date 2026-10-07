package com.pendezzapizza.pendezzapizza_api.domain.model.enums;

public enum SaleIncludeField {
    PRODUCTS,
    CUSTOMERS;

    public static SaleIncludeField fromString(String value) {
        return valueOf(value.trim().toUpperCase());
    }
}