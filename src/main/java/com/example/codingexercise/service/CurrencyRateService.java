package com.example.codingexercise.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CurrencyRateService {

    private final RestTemplate restTemplate;

    public CurrencyRateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String currencyForLocale(String locale) {
        if (locale == null || locale.isBlank()) {
            return "USD";
        }

        return switch (locale) {
            case "en-GB" -> "GBP";
            case "fr-FR" -> "EUR";
            case "ja-JP" -> "JPY";
            default -> "USD";
        };
    }

    public double getUsdToCurrencyRate(String currency) {
        if ("USD".equals(currency)) {
            return 1.0;
        }

        try {
            Map<?, ?> response = restTemplate.getForObject(
                    "https://api.frankfurter.app/latest?from=USD&to={currency}",
                    Map.class,
                    currency);

            if (response == null || !response.containsKey("rates")) {
                return 1.0;
            }

            Object ratesObject = response.get("rates");
            if (!(ratesObject instanceof Map<?, ?> rates)) {
                return 1.0;
            }

            Object rateValue = rates.get(currency);
            if (rateValue instanceof Number number) {
                return number.doubleValue();
            }
        } catch (Exception ignored) {
            return 1.0;
        }

        return 1.0;
    }

    public int priceToUsd(double price, String currency) {
        double rate = getUsdToCurrencyRate(currency);

        long cents = Math.round(price * 100);

        if (rate == 0) {
            return (int) cents;
        }
        return (int) ((cents / rate)); // Store as integer cents
    }
}
