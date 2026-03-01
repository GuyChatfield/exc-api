package com.example.codingexercise.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "packages")
public class ProductPackage {
    @Id
    private String id;
    private String name;
    private String description;
    private Map<String, Integer> productQuantities;
    private String ownerUsername;

    public ProductPackage() {
    }

    public ProductPackage(String id, String name, String description, Map<String, Integer> productQuantities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productQuantities = productQuantities;
    }

    public ProductPackage(String id, String name, String description, Map<String, Integer> productQuantities,
            String ownerUsername) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.productQuantities = productQuantities;
        this.ownerUsername = ownerUsername;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Integer> getProductQuantities() {
        return productQuantities;
    }

    public void setProductQuantities(Map<String, Integer> productQuantities) {
        this.productQuantities = productQuantities;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}
