package com.example.codingexercise.controller.dto;

import java.math.BigDecimal;

public class LocalizedProductResponse {
    private String id;
    private String name;
    private BigDecimal price;
    private String currency;
    private String locale;

    public LocalizedProductResponse(String id, String name, BigDecimal price, String currency, String locale) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.locale = locale;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getLocale() {
        return locale;
    }
}
