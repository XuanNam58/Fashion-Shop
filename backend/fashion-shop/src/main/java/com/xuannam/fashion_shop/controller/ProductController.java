package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductController {
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductsHandler(@RequestParam String category,
                                                             @RequestParam(name = "color", required = false) List<String> colors,
                                                             @RequestParam(name = "size", required = false) List<String> sizes,
                                                             @RequestParam Integer minPrice,
                                                             @RequestParam Integer maxPrice,
                                                             @RequestParam Integer minDiscount,
                                                             @RequestParam String sort,
                                                             @RequestParam String stock,
                                                             @RequestParam Integer pageNumber,
                                                             @RequestParam Integer pageSize) {


        colors = colors != null ? colors : Collections.emptyList();
        sizes = sizes != null ? sizes : Collections.emptyList();


        Page<Product> res = productService.getAllProduct(category, colors, sizes, minPrice, maxPrice,
                minDiscount, sort, stock, pageNumber, pageSize);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/get-products-by-category")
    public ResponseEntity<?> findProductByCategoryHandler(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> categories
    ) {
        if (categories != null && !categories.isEmpty()) {
            Map<String, List<Product>> result = new HashMap<>();
            for (String cat : categories) {
                result.put(cat, productService.findProductByCategory(cat));
            }
            return ResponseEntity.ok(result);
        } else if (category != null) {
            return ResponseEntity.ok(productService.findProductByCategory(category));
        } else {
            return ResponseEntity.ok(productService.findAllProducts());
        }

    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/products/suggestions")
    public ResponseEntity<List<Product>> getProductSuggestions(
            @RequestParam String q,
            @RequestParam(defaultValue = "5") int limit) {
        List<Product> suggestions = productService.getProductSuggestions(q, limit);
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/products/search")
    public ResponseEntity<Page<Product>> searchProducts(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort.split(",")[1]), sort.split(",")[0]));
        Page<Product> products = productService.searchProducts(q, pageable);
        return ResponseEntity.ok(products);
    }

}
