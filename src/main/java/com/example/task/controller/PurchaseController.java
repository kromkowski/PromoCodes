package com.example.task.controller;

import com.example.task.DTO.PurchaseDTO;
import com.example.task.service.ProductService;
import com.example.task.service.PromoCodeService;
import com.example.task.service.PurchaseService;
import dnl.utils.text.table.TextTable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final PromoCodeService promoCodeService;

    public PurchaseController(PurchaseService purchaseService, ProductService productService, PromoCodeService promoCodeService) {
        this.purchaseService = purchaseService;
        this.productService = productService;
        this.promoCodeService = promoCodeService;
    }

    @PostMapping()
    public ResponseEntity<?> createPurchase(@RequestParam(required = true) Long productID, @RequestParam(required = false) String code) {
        var product = productService.getProduct(productID);
        PurchaseDTO purchaseDTO;
        String warning = new String();
        if (code != null && !code.isEmpty()) {
            var promoCode = promoCodeService.getPromoCode(code);
            if(promoCode.getCurrencyCode().equals(product.getCurrencyCode())){
                purchaseDTO = new PurchaseDTO(product, promoCodeService.usePromoCode(code));
            } else {
                warning = "Promo code is not applicable for this product";
                purchaseDTO = new PurchaseDTO(product, BigDecimal.ZERO);
            }

        } else {
            purchaseDTO = new PurchaseDTO(product, BigDecimal.ZERO);
        }
        if(!warning.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).header("Warning", warning).body(purchaseService.createPurchase(purchaseDTO));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(purchaseDTO));
    }

    @GetMapping()
    public ResponseEntity<?> getAllPurchases() {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPurchase(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.getPurchase(id));
    }

    @GetMapping(value ="/sales-report", produces = "text/plain")
    public ResponseEntity<?> getSalesReport() {
        return ResponseEntity.ok(purchaseService.printSalesReport().toString());
    }

}
