package com.nagoya.tracker.controller;

import com.nagoya.tracker.domain.ExchangeRateResponse;
import com.nagoya.tracker.domain.Product;
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
    private final ExchangeRateResponse exchangeRateResponse;

    // HTTP GET Mapping (URL: /api/products/lowest-price)
    @GetMapping("/lowest-price")
    public String getLowestPrice() {
        Optional<Product> lowestPriceProduct = productService.getLowestPriceProduct();

        // 여기부터 다시

        return lowestPriceProduct.map(p ->
                String.format("最安値の商品情報を確認いたしました。店舗名: %s, 価格: %d円", p.getStoreName(), p.getPrice())
        ).orElse("誠に恐れ入りますが、リストが空のため、最安値を算出できませんでした。");
    }
}
