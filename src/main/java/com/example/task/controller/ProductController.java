package com.example.task.controller;


import com.example.task.DTO.ProductDTO;
import com.example.task.service.ProductService;
import com.example.task.service.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final PromoCodeService promoCodeService;

    public ProductController(ProductService productService, PromoCodeService promoCodeService) {
        this.productService = productService;
        this.promoCodeService = promoCodeService;
    }

    @PostMapping()
    public ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    @GetMapping()
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProduct(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProduct(productDTO, id));
    }

    @GetMapping("/{id}/calculate-discount")
    public ResponseEntity<?> calculateDiscount(@PathVariable Long id, @RequestParam String code) {
        var product = productService.getProduct(id);
        BigDecimal discountedPrice;
        if (code != null && !code.isEmpty()) {
            var promoCode = promoCodeService.getPromoCode(code);
            Object[] data = promoCodeService.usePromoCode(product, promoCode, false);
            discountedPrice = (product.getPrice()).subtract((BigDecimal) data[0]);
            String warning = (String) data[1];
            if (!warning.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).header("Warning", warning).body("Price with discount applied: " + discountedPrice);
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).header("Warning", "No code was given!").body("Price with discount applied: " + product.getPrice());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Price with discount applied: " + discountedPrice);
    }


}
