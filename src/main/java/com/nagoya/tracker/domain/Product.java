package com.nagoya.tracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storeName;
    private String itemName;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PriceHistory> priceHistories = new ArrayList<>();

    public Product(String storeName, String itemName) {
        this.storeName = storeName;
        this.itemName = itemName;
    }

    public int getLatestPrice() {
        return priceHistories.stream()
                .max(Comparator.comparing(PriceHistory::getRecordDate))
                .map(PriceHistory::getPrice)
                .orElse(0);
    }
}
