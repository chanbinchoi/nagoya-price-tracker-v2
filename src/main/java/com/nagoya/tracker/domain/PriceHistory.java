package com.nagoya.tracker.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private int price;

    private LocalDateTime recordDate;

    public PriceHistory(Product product, int price, LocalDateTime recordDate) {
        this.product = product;
        this.price = price;
        this.recordDate = recordDate;
    }
}
