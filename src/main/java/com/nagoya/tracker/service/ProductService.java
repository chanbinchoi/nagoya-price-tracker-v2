package com.nagoya.tracker.service;

import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service // Spring Service
@RequiredArgsConstructor // Auto Constructor (Lombok)
public class ProductService {

    private final ProductRepository productRepository;

    public Optional<Product> getLowestPriceProduct() {
        List<Product> products = productRepository.findAllWithPriceHistories();
        return products.stream()
                .filter(p -> p.getLatestPrice() > 0)
                .min(Comparator.comparingInt(Product::getLatestPrice));
    }
}
