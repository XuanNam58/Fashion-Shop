package com.xuannam.fashion_shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NotNull
    @jakarta.validation.constraints.Size(max = 50)
    String name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    Category parentCategory;

    int level;

}
