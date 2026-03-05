package com.example.inventory.module_14_assignment.repository;

import com.example.inventory.module_14_assignment.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySku(String sku);
}

