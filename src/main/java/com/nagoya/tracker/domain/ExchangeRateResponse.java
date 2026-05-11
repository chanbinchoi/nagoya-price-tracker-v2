package com.nagoya.tracker.domain;

import lombok.Getter;

import java.util.Map;

@Getter
public class ExchangeRateResponse {
    // JSON "base" ("JPY")
    private String base;
    // JSON "rates"
    private Map<String, Double> rates;
}
