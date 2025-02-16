package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductController {
    ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandler(@RequestParam String category,
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
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/products")
//    public ResponseEntity<Page<Product>> findProductByCategoryHandler(
//            @RequestParam String category,
//            @RequestParam(required = false) List<String> colors,
//            @RequestParam(required = false) List<String> sizes,
//            @RequestParam Integer minPrice,
//            @RequestParam Integer maxPrice,
//            @RequestParam Integer minDiscount,
//            @RequestParam String sort,
//            @RequestParam(required = false) String stock,  // ✅ Cho phép null
//            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,  // ✅ Mặc định là 0
//            @RequestParam(required = false, defaultValue = "10") Integer pageSize) { // ✅ Mặc định là 10
//
//        // Nếu colors hoặc sizes bị null, gán danh sách rỗng để tránh lỗi
//        if (colors == null) colors = new ArrayList<>();
//        if (sizes == null) sizes = new ArrayList<>();
//
//        Page<Product> res = productService.getAllProduct(category, colors, sizes, minPrice, maxPrice,
//                minDiscount, sort, stock, pageNumber, pageSize);
//        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
//    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByHandler(@PathVariable Long productId) throws ProductException {
        Product product = productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/products/search")
//    public ResponseEntity<List<Product>> searchProductHandler(@RequestParam String q) {
//        List
//    }

}
