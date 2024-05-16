package com.example.task.service;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.mapper.PromoCodeMapper;
import com.example.task.model.PromoCode;
import com.example.task.repository.PromoCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCode createPromoCode(PromoCodeDTO promoCodeDTO) {
        if(promoCodeRepository.findFirstByCode(promoCodeDTO.getCode()) != null) {
            System.out.println("Promo code with such code already exists.");
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Promo code with such code already exists."
            );
        }
        try {
            PromoCode promoCode = PromoCodeMapper.toPromoCode(promoCodeDTO);
            return promoCodeRepository.save(promoCode);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Bad request. Check the PromoCodeDTO fields and try again."
            );
        }
    }

    public PromoCode getPromoCode(String code) {
        var promoCode = promoCodeRepository.findFirstByCode(code);
        if(promoCode == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Promo code of such id was not found."
            );
        }
        return promoCode;
    }

    public List<PromoCode> getAllPromoCodes() {
        var promoCodes = promoCodeRepository.findAll();
        if(promoCodes.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "No promo codes found."
            );
        }
        return promoCodes;
    }

    public BigDecimal usePromoCode(String code) {
        System.out.println("Using promo code: " + code);
        var promoCode = getPromoCode(code);
        if(promoCode.getRemainingUsages() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Promo code has no usages left."
            );
        }
        promoCode.setRemainingUsages(promoCode.getRemainingUsages() - 1);
        return promoCodeRepository.save(promoCode).getDiscount();
    }


}
