package com.nagoya.tracker.repository;

import com.nagoya.tracker.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Auto CRUD SQL
public interface ProductRepository extends JpaRepository<Product, Long> {
}
