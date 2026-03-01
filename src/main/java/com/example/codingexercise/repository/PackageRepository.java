package com.example.codingexercise.repository;

import com.example.codingexercise.exception.PackageNotFoundException;
import com.example.codingexercise.model.ProductPackage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PackageRepository {

    private final PackageMongoRepository packageMongoRepository;

    public PackageRepository(PackageMongoRepository packageMongoRepository) {
        this.packageMongoRepository = packageMongoRepository;
    }

    public ProductPackage create(String name, String description, java.util.Map<String, Integer> productQuantities) {
        return create(name, description, productQuantities, null);
    }

    public ProductPackage create(String name, String description, java.util.Map<String, Integer> productQuantities,
            String ownerUsername) {
        ProductPackage newProductPackage = new ProductPackage(
                UUID.randomUUID().toString(),
                name,
                description,
                productQuantities,
                ownerUsername);
        return packageMongoRepository.save(newProductPackage);
    }

    public Optional<ProductPackage> get(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return packageMongoRepository.findById(id);
    }

    public ProductPackage update(String id, ProductPackage updatedPackage) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID cannot be null or blank");
        }
        if (!packageMongoRepository.existsById(id)) {
            throw new PackageNotFoundException(id);
        }
        updatedPackage.setId(id);
        return packageMongoRepository.save(updatedPackage);
    }

    public boolean delete(String id) {
        if (id == null || id.isBlank() || !packageMongoRepository.existsById(id)) {
            return false;
        }
        packageMongoRepository.deleteById(id);
        return true;
    }

    public List<ProductPackage> list() {
        return packageMongoRepository.findAll();
    }

    public List<ProductPackage> listByOwnerUsername(String ownerUsername) {
        return packageMongoRepository.findByOwnerUsername(ownerUsername);
    }
}
