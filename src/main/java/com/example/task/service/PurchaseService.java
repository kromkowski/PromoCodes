package com.example.task.service;

import com.example.task.DTO.PurchaseDTO;
import com.example.task.mapper.PurchaseMapper;
import com.example.task.model.Purchase;
import com.example.task.repository.PurchaseRepository;
import dnl.utils.text.table.TextTable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Purchase createPurchase(PurchaseDTO purchaseDTO) {
        try {
            Purchase purchase = PurchaseMapper.toPurchase(purchaseDTO);
            return purchaseRepository.save(purchase);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    public Purchase getPurchase(Long id) {
        var purchase = purchaseRepository.findFirstById(id);
        if(purchase == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Purchase of such id was not found."
            );
        }
        return purchase;
    }

    public List<Purchase> getAllPurchases() {
        var purchases = purchaseRepository.findAll();
        if(purchases.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "No purchases found."
            );
        }
        return purchases;
    }

    public String printSalesReport(){
        List<Purchase> purchases = getAllPurchases();
        HashSet<String> currencies = new HashSet<>();
        for(Purchase purchase : purchases){
            currencies.add(purchase.getProduct().getCurrencyCode());
        }
        Object [][] reportData = new Object[currencies.size()][4];
        int index = 0;
        for(String currency : currencies){
            int noOfPurchases = 0;
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal totalDiscount = BigDecimal.ZERO;
            for(Purchase purchase : purchases){
                if(purchase.getProduct().getCurrencyCode().equals(currency)){
                    noOfPurchases++;
                    totalAmount = totalAmount.add(purchase.getRegularPrice());
                    totalDiscount = totalDiscount.add(purchase.getDiscountValue());
                }
            }
            reportData[index][0] = currency;
            reportData[index][1] = totalAmount;
            reportData[index][2] = totalDiscount;
            reportData[index][3] = noOfPurchases;
            index++;
        }
        String[] columnNames = {"Currency", "Total amount", "Total discount", "No of purchases"};
        TextTable report= new TextTable(columnNames, reportData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        report.printTable(printStream, 0);
        return outputStream.toString();
    }

}
