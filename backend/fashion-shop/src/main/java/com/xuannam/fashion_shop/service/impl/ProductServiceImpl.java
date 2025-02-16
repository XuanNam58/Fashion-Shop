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
    public List<Product> findProductByCategory(java.lang.String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
        /*filter sẽ giữ lại các phần tử thỏa mãn điều kiện
         * anyMatch trả về boolean nếu xuất hiện phần tử thỏa mãn điều kiện*/
        if (!colors.isEmpty()) {
            products = products.stream().filter(product ->
                    colors.stream().anyMatch(c ->
                            c.equalsIgnoreCase(product.getColor()))).collect(Collectors.toList());
        }

        if (stock != null) {
            if (stock.equals("in_stock")) {
                products = products.stream().filter(product -> product.getQuantity() > 0).collect(Collectors.toList());
            } else if (stock.equals("out_of_stock")) {
                products = products.stream().filter(product -> product.getQuantity() < 0).collect(Collectors.toList());
            }
        }

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());

        List<Product> pageContent = products.subList(startIndex, endIndex);

        return new PageImpl<>(pageContent, pageable, products.size());
    }
}
