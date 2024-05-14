package com.example.task.repository;

import com.example.task.model.Product;
import com.example.task.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findFirstById(Long id);
    List<Product> findAll();
}
