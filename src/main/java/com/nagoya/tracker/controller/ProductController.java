package com.nagoya.tracker.controller;

import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.domain.ProductResponse;
import com.nagoya.tracker.service.ExchangeRateService;
import com.nagoya.tracker.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController // REST API
@RequestMapping("/api/products") // Default URL
@RequiredArgsConstructor // Auto Constructor (Lombok)
public class ProductController {

    private final ProductService productService;
    private final ExchangeRateService exchangeRateService;

    // HTTP GET Mapping (URL: /api/products/lowest-price)
    @GetMapping("/lowest-price")
    public ProductResponse getLowestPrice() {
        Optional<Product> lowestPriceProduct = productService.getLowestPriceProduct();
        double jpyToKrw = exchangeRateService.getJpyToKrwRate();

        if (lowestPriceProduct.isPresent()) {
            Product p = lowestPriceProduct.get();
            int priceInKrw = (int) (p.getPrice() * jpyToKrw);

            return new ProductResponse(
                    true,
                    p.getStoreName(),
                    p.getPrice(),
                    priceInKrw,
                    jpyToKrw,
                    "最安値の取得に成功しました。"
            );
        } else {
            // Fail
            return new ProductResponse(
                    false, "", 0, 0, 0.0,
                    "リストが空のため、最安値を算出できませんでした。"
            );
        }
    }
}
