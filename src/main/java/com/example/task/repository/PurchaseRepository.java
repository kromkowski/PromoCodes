package com.example.task.repository;

import com.example.task.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Purchase findFirstById(Long id);
    List<Purchase> findAll();
}
