package com.example.task.controller;

import com.example.task.DTO.PromoCodeDTO;
import com.example.task.service.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promocodes")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @PostMapping()
    public ResponseEntity<?> createPromoCode(@RequestBody PromoCodeDTO promoCodeDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(promoCodeService.createPromoCode(promoCodeDTO));
    }

    @GetMapping()
    public ResponseEntity<?> getAllPromoCodes() {
        return ResponseEntity.status(HttpStatus.OK).body(promoCodeService.getAllPromoCodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPromoCode(@PathVariable String code) {
        return ResponseEntity.status(HttpStatus.OK).body(promoCodeService.getPromoCode(code));
    }
}
