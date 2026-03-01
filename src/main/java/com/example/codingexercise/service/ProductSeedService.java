package com.example.codingexercise.service;

import com.example.codingexercise.model.Product;
import com.example.codingexercise.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(2)
public class ProductSeedService implements CommandLineRunner {

    private final ProductRepository productRepository;

    public ProductSeedService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        List<Product> seedProducts = List.of(
                new Product("VqKb4tyj9V6i", "Shield", 1149),
                new Product("DXSQpv6XVeJm", "Helmet", 999),
                new Product("7dgX6XzU3Wds", "Sword", 899),
                new Product("PKM5pGAh9yGm", "Axe", 799),
                new Product("7Hv0hA2nmci7", "Knife", 349),
                new Product("500R5EHvNlNB", "Gold Coin", 249),
                new Product("IP3cv7TcZhQn", "Platinum Coin", 399),
                new Product("IJOHGYkY2CYq", "Bow", 649),
                new Product("8anPsR2jbfNW", "Silver Coin", 50));

        for (Product product : seedProducts) {
            if (!productRepository.existsById(java.util.Objects.requireNonNull(product.getId()))) {
                productRepository.save(product);
            }
        }
    }
}
