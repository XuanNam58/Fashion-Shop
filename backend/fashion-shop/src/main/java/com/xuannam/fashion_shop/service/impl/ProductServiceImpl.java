package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.resquest.CreateProductRequest;
import com.xuannam.fashion_shop.entity.Category;
import com.xuannam.fashion_shop.entity.Product;

import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.CategoryRepository;
import com.xuannam.fashion_shop.repository.ProductRepository;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    .name(request.getTopLevelCategory())
                    .level(1)
                    .build();
            topLevel = categoryRepository.save(topLevelCategory);
        }

        Category secondLevel = categoryRepository
                .findByNameAndParent(request.getSecondLevelCategory(), topLevel.getName());
        if (secondLevel == null) {
            Category secondLevelCategory = Category.builder()
                    .name(request.getSecondLevelCategory())
                    .parentCategory(topLevel)
                    .level(2)
                    .build();
            secondLevel = categoryRepository.save(secondLevelCategory);
        }

        Category thirdLevel = categoryRepository
                .findByNameAndParent(request.getThirdLevelCategory(), secondLevel.getName());
        if (thirdLevel == null) {
            Category thirdLevelCategory = Category.builder()
                    .name(request.getThirdLevelCategory())
                    .parentCategory(secondLevel)
                    .level(3)
                    .build();
            thirdLevel = categoryRepository.save(thirdLevelCategory);
        }

        Product product = Product.builder()
                .title(request.getTitle())
                .color(request.getColor())
                .description(request.getDescription())
                .discountedPrice(request.getDiscountedPrice())
                .discountPercent(request.getDiscountPercent())
                .imageUrl(request.getImageUrl())
                .brand(request.getBrand())
                .price(request.getPrice())
                .sizes(request.getSizes())
                .quantity(request.getQuantity())
                .category(thirdLevel)
                .createdAt(LocalDateTime.now())
                .build();

        return productRepository.save(product);
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = findProductById(productId);
        product.getSizes().clear();
        productRepository.delete(product);
        return "Product deleted Successfully";
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long productId, Product request) throws ProductException {
        Product product = findProductById(productId);

        if (request.getQuantity() != 0) {
            product.setQuantity(request.getQuantity());
        }

        if (request.getSizes() != null && !request.getSizes().isEmpty()) {
            product.setSizes(request.getSizes());
        }
        return productRepository.save(product);
    }

    @Override
    public Product findProductById(Long productId) throws ProductException {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ProductException("Product not found with id - " + productId);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return productRepository.findProductByCategory(category);
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) throws ProductException {
        if (pageNumber < 0 || pageSize <= 0) {
            throw new ProductException("Invalid pageNumber or pageSize");
        }
        List<String> filteredColors = colors != null && !colors.isEmpty() ? colors.stream().map(String::toLowerCase).toList() : null;
        List<String> filteredSizes = sizes != null && !sizes.isEmpty() ? sizes.stream().map(String::toLowerCase).toList() : null;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, filteredColors, filteredSizes, stock, sort, pageable);
    }

    @Override
    public List<Product> getProductSuggestions(String q, int limit) {
        if (q == null || q.trim().isEmpty()) return Collections.emptyList();
        Pageable pageable = PageRequest.of(0, limit);
        return productRepository.getProductSuggestions(q, pageable);
    }

    @Override
    public Page<Product> searchProducts(String q, Pageable pageable) {
        if (q == null || q.trim().isEmpty()) {
            return Page.empty(); // Trả về trang rỗng nếu không có từ khóa
        }
        return productRepository.searchProducts(q, pageable);
    }
}
