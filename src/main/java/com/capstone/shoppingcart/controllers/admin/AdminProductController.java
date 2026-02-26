package com.capstone.shoppingcart.controllers.admin;

import com.capstone.shoppingcart.dtos.CreateProductRequest;
import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.dtos.UpdateProductRequest;
import com.capstone.shoppingcart.services.admin.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/v1/products")
@RequiredArgsConstructor
public class AdminProductController {
    
    private final AdminProductService adminProductService;
    
    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(Pageable pageable) {
        Page<ProductResponseDto> products = adminProductService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductResponseDto product = adminProductService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {
        ProductResponseDto product = adminProductService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
