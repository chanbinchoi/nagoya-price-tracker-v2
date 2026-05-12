package com.nagoya.tracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private boolean success;
    private String storeName;
    private int priceJpy;
    private int priceKrw;
    private double exchangeRate;
    private String message;
}
