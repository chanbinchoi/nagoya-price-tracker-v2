package com.nagoya.tracker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // JPA
@Getter // Auto Getter (Lombok)
@NoArgsConstructor // Auto Constructor - NoArgs
@AllArgsConstructor // Auto Constructor - AllArgs
public class Product {

    @Id // PK
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB Auto Increase ID
    private Long id;

    private String storeName;
    private String itemName;
    private int price;

    // Constructor
    public Product(String storeName, String itemName, int price) {
        this.storeName = storeName;
        this.itemName = itemName;
        this.price = price;
    }
}
