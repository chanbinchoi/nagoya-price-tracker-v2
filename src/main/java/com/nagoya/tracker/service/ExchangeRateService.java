package com.nagoya.tracker.service;

import com.nagoya.tracker.domain.ExchangeRateResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExchangeRateService {

    // Spring built-in object responsible for external API communication
    private final RestTemplate restTemplate = new RestTemplate();

    public double getJpyToKrwRate() {
        String url = "https://api.exchangerate-api.com/v4/latest/JPY";

        // HTTP GET, Return ExchangeRateResponse(Deserialize)
        ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

        // Extract, Return Rate JPY <-> KRW
        if (response != null && response.getRates() != null) {
            return response.getRates().get("KRW");
        }
        return 9.0; // Default rate (API failure)

    }
}
