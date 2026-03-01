package com.example.codingexercise.controller;

import com.example.codingexercise.controller.dto.LocalizedProductResponse;
import com.example.codingexercise.model.Product;
import com.example.codingexercise.repository.ProductRepository;
import com.example.codingexercise.service.CurrencyRateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
public class ProductController {

    private final ProductRepository productRepository;
    private final CurrencyRateService currencyRateService;

    public ProductController(ProductRepository productRepository, CurrencyRateService currencyRateService) {
        this.productRepository = productRepository;
        this.currencyRateService = currencyRateService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/products")
    public Product create(@RequestParam String name, @RequestParam double price, @RequestParam String currency) {
        Product product = new Product();
        product.setName(name);
        product.setUsdPrice(currencyRateService.priceToUsd(price, currency));
        return productRepository.save(product);
    }

    @GetMapping("/products")
    public List<LocalizedProductResponse> list(@RequestParam(required = false) String locale) {
        String effectiveLocale = (locale == null || locale.isBlank()) ? "en-US" : locale;
        String currency = currencyRateService.currencyForLocale(effectiveLocale);
        double rate = currencyRateService.getUsdToCurrencyRate(currency);
        int scale = "JPY".equals(currency) ? 0 : 2;

        return productRepository.findAll().stream().map(product -> {
            // usdPrice is stored as cents, so divide by 100 to get dollars before conversion
            BigDecimal priceInDollars = BigDecimal.valueOf(product.getUsdPrice())
                    .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal convertedPrice = priceInDollars
                    .multiply(BigDecimal.valueOf(rate))
                    .setScale(scale, RoundingMode.HALF_UP);

            return new LocalizedProductResponse(
                    product.getId(),
                    product.getName(),
                    convertedPrice,
                    currency,
                    effectiveLocale);
        }).toList();
    }
}
