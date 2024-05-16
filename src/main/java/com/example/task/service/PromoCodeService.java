package com.example.task.service;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.mapper.PromoCodeMapper;
import com.example.task.model.Product;
import com.example.task.model.PromoCode;
import com.example.task.model.PromoCodePercentage;
import com.example.task.model.PromoCodeValue;
import com.example.task.repository.PromoCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;

    public PromoCodeService(PromoCodeRepository promoCodeRepository) {
        this.promoCodeRepository = promoCodeRepository;
    }

    public PromoCode createPromoCode(PromoCodeDTO promoCodeDTO) {
        if (promoCodeRepository.findFirstByCode(promoCodeDTO.getCode()) != null) {
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
        var promoCode = promoCodeRepository.findFirstByCode(code.toUpperCase());
        if (promoCode == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Promo code of such id was not found."
            );
        }
        return promoCode;
    }

    public List<PromoCode> getAllPromoCodes() {
        var promoCodes = promoCodeRepository.findAll();
        if (promoCodes.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "No promo codes found."
            );
        }
        return promoCodes;
    }

    public Object[] usePromoCode(Product product, PromoCode promoCode, boolean isToBeSaved) {
        Object[] result = new Object[2];
        String warning = "";
        BigDecimal discount = BigDecimal.ZERO; //ustawić format na currency
        if (promoCode.getRemainingUses() == 0) {
            warning = "Promo code has no remaining uses.";
        } else {
            if (promoCode.getCurrencyCode().equals(product.getCurrencyCode()) && !promoCode.getExpirationDate().isBefore(LocalDate.now())) {
                if (promoCode instanceof PromoCodePercentage) {//mozliwe, ze bedzie zle liczylo
                    var percentageToDecimal = ((PromoCodePercentage) promoCode).getDiscountPercentage().divide(BigDecimal.valueOf(100), 2, RoundingMode.UP);
                    discount = product.getPrice().multiply(percentageToDecimal).setScale(product.getPrice().scale(), RoundingMode.UP);
                } else if (promoCode instanceof PromoCodeValue) {
                    var promoCodeDiscount = ((PromoCodeValue) promoCode).getDiscountValue();
                    if (product.getPrice().compareTo(promoCodeDiscount) <= 0) {
                        discount = product.getPrice().setScale(product.getPrice().scale(), RoundingMode.UP);
                    } else {
                        discount = promoCodeDiscount.setScale(product.getPrice().scale(), RoundingMode.UP);
                    }
                }
                if (isToBeSaved) {
                    promoCode.setRemainingUses(promoCode.getRemainingUses() - 1);
                    promoCodeRepository.save(promoCode);
                }
            } else {
                warning = "Promo code is not applicable for this product";
            }
        }
        result[0] = discount;
        result[1] = warning;
        return result;
    }


}
