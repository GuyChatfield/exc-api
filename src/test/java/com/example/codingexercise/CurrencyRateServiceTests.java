package com.example.codingexercise;

import com.example.codingexercise.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CurrencyRateServiceTests {

    private final CurrencyRateService currencyRateService;

    @Autowired
    CurrencyRateServiceTests(CurrencyRateService currencyRateService) {
        this.currencyRateService = currencyRateService;
    }

    @Test
    void currencyForLocale_enUS() {
        assertEquals("USD", currencyRateService.currencyForLocale("en-US"));
    }

    @Test
    void currencyForLocale_enGB() {
        assertEquals("GBP", currencyRateService.currencyForLocale("en-GB"));
    }

    @Test
    void currencyForLocale_frFR() {
        assertEquals("EUR", currencyRateService.currencyForLocale("fr-FR"));
    }

    @Test
    void currencyForLocale_jaJP() {
        assertEquals("JPY", currencyRateService.currencyForLocale("ja-JP"));
    }

    @Test
    void currencyForLocale_nullReturnsUSD() {
        assertEquals("USD", currencyRateService.currencyForLocale(null));
    }

    @Test
    void currencyForLocale_blankReturnsUSD() {
        assertEquals("USD", currencyRateService.currencyForLocale(""));
        assertEquals("USD", currencyRateService.currencyForLocale("   "));
    }

    @Test
    void currencyForLocale_unknownLocaleReturnsUSD() {
        assertEquals("USD", currencyRateService.currencyForLocale("de-DE"));
        assertEquals("USD", currencyRateService.currencyForLocale("unknown"));
    }

    @Test
    void getUsdToCurrencyRate_USDReturnsOne() {
        assertEquals(1.0, currencyRateService.getUsdToCurrencyRate("USD"));
    }

    @Test
    void getUsdToCurrencyRate_GBPReturnsPositive() {
        double rate = currencyRateService.getUsdToCurrencyRate("GBP");
        assertTrue(rate > 0, "GBP rate should be positive");
        // GBP is generally less than 1 USD (e.g., 0.7-0.85)
        assertTrue(rate < 1.5, "GBP rate seems unrealistic");
    }

    @Test
    void getUsdToCurrencyRate_EURReturnsPositive() {
        double rate = currencyRateService.getUsdToCurrencyRate("EUR");
        assertTrue(rate > 0, "EUR rate should be positive");
    }

    @Test
    void getUsdToCurrencyRate_JPYReturnsHigherValue() {
        double rate = currencyRateService.getUsdToCurrencyRate("JPY");
        assertTrue(rate > 0, "JPY rate should be positive");
        // JPY is typically 100+ per USD
        assertTrue(rate > 50, "JPY rate seems unrealistic (should be high)");
    }

    @Test
    void priceToUsd_fromUSD() {
        // $19.99 should become 1999 cents
        int cents = currencyRateService.priceToUsd(19.99, "USD");
        assertEquals(1999, cents);
    }

    @Test
    void priceToUsd_fromUSD_wholeDollar() {
        // $10.00 should become 1000 cents
        int cents = currencyRateService.priceToUsd(10.00, "USD");
        assertEquals(1000, cents);
    }

    @Test
    void priceToUsd_fromOtherCurrency() {
        // This will convert based on exchange rate - just ensure it's positive
        int cents = currencyRateService.priceToUsd(10.00, "GBP");
        assertTrue(cents > 0, "Converted price should be positive");
        // GBP is worth more than USD, so 10 GBP > 10 USD (1000 cents)
        assertTrue(cents > 1000, "10 GBP should convert to more than 1000 USD cents");
    }

    @Test
    void priceToUsd_zeroPrice() {
        int cents = currencyRateService.priceToUsd(0.0, "USD");
        assertEquals(0, cents);
    }

    @Test
    void priceToUsd_smallPrice() {
        // $0.01 should become 1 cent
        int cents = currencyRateService.priceToUsd(0.01, "USD");
        assertEquals(1, cents);
    }
}
