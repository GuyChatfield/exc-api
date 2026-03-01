package com.example.codingexercise;

import com.example.codingexercise.controller.dto.LocalizedProductResponse;
import com.example.codingexercise.model.Product;
import com.example.codingexercise.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTests {

    private final TestRestTemplate restTemplate;
    private final ProductRepository productRepository;

    @Autowired
    ProductControllerTests(TestRestTemplate restTemplate, ProductRepository productRepository) {
        this.restTemplate = restTemplate;
        this.productRepository = productRepository;
    }

    @Test
    void createProduct() {
        String name = "Test Product " + System.currentTimeMillis();
        double price = 19.99;
        String currency = "USD";

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "/products?name={name}&price={price}&currency={currency}",
                null, Product.class, name, price, currency);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product product = response.getBody();
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(name, product.getName());
        // Price stored in cents: 19.99 * 100 = 1999
        assertEquals(1999, product.getUsdPrice());
    }

    @Test
    void createProduct_withDifferentCurrency() {
        String name = "Euro Product " + System.currentTimeMillis();
        double price = 10.00;
        String currency = "EUR";

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "/products?name={name}&price={price}&currency={currency}",
                null, Product.class, name, price, currency);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Product product = response.getBody();
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(name, product.getName());
        // USD price will vary based on exchange rate, but should be stored
        assertTrue(product.getUsdPrice() > 0);
    }

    @Test
    void listProducts_defaultLocale() {
        // Create a product first
        Product testProduct = new Product();
        testProduct.setName("List Test Product");
        testProduct.setUsdPrice(1000); // $10.00 in cents
        productRepository.save(testProduct);

        ResponseEntity<LocalizedProductResponse[]> response = restTemplate.getForEntity(
                "/products", LocalizedProductResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalizedProductResponse[] products = response.getBody();
        assertNotNull(products);
        assertTrue(products.length > 0);

        // Default locale should be en-US with USD
        LocalizedProductResponse first = products[0];
        assertEquals("USD", first.getCurrency());
        assertEquals("en-US", first.getLocale());
    }

    @Test
    void listProducts_withGBPLocale() {
        ResponseEntity<LocalizedProductResponse[]> response = restTemplate.getForEntity(
                "/products?locale=en-GB", LocalizedProductResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalizedProductResponse[] products = response.getBody();
        assertNotNull(products);

        if (products.length > 0) {
            assertEquals("GBP", products[0].getCurrency());
            assertEquals("en-GB", products[0].getLocale());
        }
    }

    @Test
    void listProducts_withEURLocale() {
        ResponseEntity<LocalizedProductResponse[]> response = restTemplate.getForEntity(
                "/products?locale=fr-FR", LocalizedProductResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalizedProductResponse[] products = response.getBody();
        assertNotNull(products);

        if (products.length > 0) {
            assertEquals("EUR", products[0].getCurrency());
            assertEquals("fr-FR", products[0].getLocale());
        }
    }

    @Test
    void listProducts_withJPYLocale() {
        ResponseEntity<LocalizedProductResponse[]> response = restTemplate.getForEntity(
                "/products?locale=ja-JP", LocalizedProductResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalizedProductResponse[] products = response.getBody();
        assertNotNull(products);

        if (products.length > 0) {
            assertEquals("JPY", products[0].getCurrency());
            assertEquals("ja-JP", products[0].getLocale());
            // JPY should have 0 decimal places
            assertEquals(0, products[0].getPrice().scale());
        }
    }

    @Test
    void listProducts_emptyLocaleDefaultsToUS() {
        ResponseEntity<LocalizedProductResponse[]> response = restTemplate.getForEntity(
                "/products?locale=", LocalizedProductResponse[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        LocalizedProductResponse[] products = response.getBody();
        assertNotNull(products);

        if (products.length > 0) {
            assertEquals("USD", products[0].getCurrency());
            assertEquals("en-US", products[0].getLocale());
        }
    }
}
