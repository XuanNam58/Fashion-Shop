package com.xuannam.fashion_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "title")
    java.lang.String title;

    @Column(name = "description")
    java.lang.String description;

    @Column(name = "price")
    int price;

    @Column(name = "discounted_price")
    int discountedPrice;

    @Column(name = "discount_percent")
    int discountPercent;

    @Column(name = "quantity")
    int quantity;

    @Column(name = "brand")
    java.lang.String brand;

    @Column(name = "color")
    java.lang.String color;

    @Embedded
    @ElementCollection
    @Column(name = "sizes")
    Set<Size> sizes = new HashSet<>();

    @Column(name = "image_url")
    java.lang.String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews = new ArrayList<>();

    @Column(name = "num_ratings")
    int numRatings;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    LocalDateTime createdAt;

}
