package com.example.codingexercise.repository;

import com.example.codingexercise.model.ProductPackage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PackageMongoRepository extends MongoRepository<ProductPackage, String> {
    List<ProductPackage> findByOwnerUsername(String ownerUsername);
}
