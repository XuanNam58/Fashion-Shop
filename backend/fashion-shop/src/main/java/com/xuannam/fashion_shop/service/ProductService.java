package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.resquest.CreateProductRequest;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Size;
import com.xuannam.fashion_shop.exception.ProductException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product createProduct(CreateProductRequest request);

    String deleteProduct(Long productId) throws ProductException;

    Product updateProduct(Long productId, Product product) throws ProductException;

    Product findProductById(Long productId) throws ProductException;

    List<Product> findProductByCategory(String category);

    Page<Product> getAllProduct(String category, List<String> colors, List<Size> sizes, Integer minPrice, Integer maxPrice,
                                Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize);
}
