package com.nagoya.tracker.config;

import com.nagoya.tracker.domain.Product;
import com.nagoya.tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component // Spring Bean
@RequiredArgsConstructor // Auto Constructor (Lombok)
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Data Save
        productRepository.saveAll(List.of(
                new Product("V-drug", "apple", 200),
                new Product("V-drug", "banana", 150),
                new Product("Yamanaka", "banana", 180),
                new Product("Amica", "banana", 130)
        ));

        System.out.println("Complete Init Nagoya Store Data");
    }
}
