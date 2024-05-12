package com.example.task.service;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.mapper.PromoCodeMapper;
import com.example.task.model.PromoCode;
import com.example.task.repository.PromoCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCode createPromoCode(PromoCodeDTO promoCodeDTO) {
        try {
            PromoCode promoCode = PromoCodeMapper.toPromoCode(promoCodeDTO);
            return promoCodeRepository.save(promoCode);
        } catch (Exception e) {
            throw new RuntimeException("Error creating promo code");
        }
    }

    public PromoCode getPromoCode(Long id) {
        return promoCodeRepository.findFirstById(id);
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepository.findAll();
    }


}
