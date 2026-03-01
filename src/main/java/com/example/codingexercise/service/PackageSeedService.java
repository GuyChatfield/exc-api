package com.example.codingexercise.service;

import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageMongoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@Order(3)
public class PackageSeedService implements CommandLineRunner {

    private final PackageMongoRepository packageRepository;

    public PackageSeedService(PackageMongoRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public void run(String... args) {
        List<ProductPackage> seedPackages = List.of(
                new ProductPackage(
                        "pkg-starter-001",
                        "Start Pack",
                        "Essential items for new adventurers",
                        Map.of(
                                "7Hv0hA2nmci7", 1, // Knife
                                "500R5EHvNlNB", 5 // Gold Coin
                        ),
                        "tuser"),
                new ProductPackage(
                        "pkg-adept-002",
                        "Adept Pack",
                        "Gear for experienced warriors",
                        Map.of(
                                "7dgX6XzU3Wds", 1, // Sword
                                "VqKb4tyj9V6i", 1, // Shield
                                "IP3cv7TcZhQn", 3 // Platinum Coin
                        ),
                        "tuser"),
                new ProductPackage(
                        "pkg-artisan-003",
                        "Artisan Pack",
                        "Premium collection for master craftsmen",
                        Map.of(
                                "DXSQpv6XVeJm", 1, // Helmet
                                "PKM5pGAh9yGm", 1, // Axe
                                "IJOHGYkY2CYq", 1, // Bow
                                "IP3cv7TcZhQn", 10 // Platinum Coin
                        ),
                        "tuser"));

        for (ProductPackage pkg : seedPackages) {
            if (!packageRepository.existsById(Objects.requireNonNull(pkg.getId()))) {
                packageRepository.save(pkg);
            }
        }
    }
}
