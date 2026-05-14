package com.nagoya.tracker.repository;

import com.nagoya.tracker.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByStoreNameAndItemName(String storeName, String itemName);

    @Query("SELECT DISTINCT p FROM Product p JOIN FETCH p.priceHistories")
    List<Product> findAllWithPriceHistories();
}
