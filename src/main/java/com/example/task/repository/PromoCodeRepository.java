package com.example.task.repository;

import com.example.task.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
    PromoCode findFirstById(Long id);
    List<PromoCode> findAll();
}
