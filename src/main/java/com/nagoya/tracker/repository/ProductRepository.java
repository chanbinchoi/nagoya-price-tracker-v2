package com.nagoya.tracker.repository;

import com.nagoya.tracker.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByStoreNameAndItemName(String storeName, String itemName);
}
