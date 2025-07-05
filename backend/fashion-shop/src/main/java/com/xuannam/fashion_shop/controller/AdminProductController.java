package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.response.ApiResponse;
import com.xuannam.fashion_shop.dto.resquest.CreateProductRequest;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Admin Product Controller")
public class AdminProductController {
    ProductService productService;

    @PostMapping("/")
    @Operation(summary = "Create product")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId, @RequestHeader("Authorization") String jwt) throws ProductException {
        productService.deleteProduct(productId);
        ApiResponse apiResponse = ApiResponse.builder().status(true).message("Product deleted successfully").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all products")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product request) throws ProductException {
        Product product = productService.updateProduct(productId, request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    @Operation(summary = "Create products")
    public ResponseEntity<ApiResponse> createMultipleProducts(@RequestBody CreateProductRequest[] requests) {
        for (CreateProductRequest request : requests) {
            productService.createProduct(request);
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .message("Products created successfully")
                .status(true)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
