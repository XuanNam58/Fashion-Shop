package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.resquest.CreateProductRequest;
import com.xuannam.fashion_shop.entity.Category;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Size;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.CategoryRepository;
import com.xuannam.fashion_shop.repository.ProductRepository;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    UserService userService;
    CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest request) {
        Category topLevel = categoryRepository.findByName(request.getTopLevelCategory());
        if (topLevel == null) {
            Category topLevelCategory = Category.builder()
                    .name(topLevel.getName())
                    .level(1)
                    .build();
            topLevel = categoryRepository.save(topLevelCategory);
        }
        return null;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        return null;
    }

    @Override
    public Product updateProduct(Long productId, Product product) throws ProductException {
        return null;
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        return null;
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<Size> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        return null;
    }
}
