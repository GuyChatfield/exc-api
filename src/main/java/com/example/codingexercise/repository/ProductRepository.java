package com.example.codingexercise.repository;

import com.example.codingexercise.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
