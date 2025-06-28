package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.resquest.CreateProductRequest;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Size;
import com.xuannam.fashion_shop.exception.ProductException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest request);

    String deleteProduct(Long productId) throws ProductException;
    List<Product> findAllProducts();

    Product updateProduct(Long productId, Product request) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> findProductByCategory(java.lang.String category);

    Page<Product> getAllProduct(java.lang.String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
                                Integer minDiscount, java.lang.String sort, java.lang.String stock, Integer pageNumber, Integer pageSize);
    List<Product> getProductSuggestions(String q, int limit);

    Page<Product> searchProducts(String q, Pageable pageable);
}
