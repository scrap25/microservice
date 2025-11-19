package com.microservices.inventoryservice.controller;

import com.microservices.inventory.dto.ProductDto;
import com.microservices.inventory.dto.StockReservationDto;
import com.microservices.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = inventoryService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{productCode}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String productCode) {
        ProductDto product = inventoryService.getProductByCode(productCode);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        ProductDto created = inventoryService.createProduct(productDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/products/{productCode}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable String productCode,
                                                    @RequestBody ProductDto productDto) {
        ProductDto updated = inventoryService.updateProduct(productCode, productDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/reserve")
    public ResponseEntity<Boolean> reserveStock(@RequestBody StockReservationDto reservationDto) {
        boolean reserved = inventoryService.reserveStock(reservationDto);
        return ResponseEntity.ok(reserved);
    }

    @PostMapping("/release")
    public ResponseEntity<Void> releaseStock(@RequestBody StockReservationDto reservationDto) {
        inventoryService.releaseStock(reservationDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmStock(@RequestBody StockReservationDto reservationDto) {
        inventoryService.confirmStock(reservationDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/products/{productCode}/availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String productCode,
                                                     @RequestParam Integer quantity) {
        boolean available = inventoryService.checkAvailability(productCode, quantity);
        return ResponseEntity.ok(available);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductDto>> getLowStockProducts() {
        List<ProductDto> lowStockProducts = inventoryService.getLowStockProducts();
        return ResponseEntity.ok(lowStockProducts);
    }
}