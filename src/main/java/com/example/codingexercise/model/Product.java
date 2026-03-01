package com.example.codingexercise.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private int usdPrice;

    public Product() {
    }

    public Product(String id, String name, int usdPrice) {
        this.id = id;
        this.name = name;
        this.usdPrice = usdPrice;
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

    public int getUsdPrice() {
        return usdPrice;
    }

    public void setUsdPrice(int usdPrice) {
        this.usdPrice = usdPrice;
    }
}
