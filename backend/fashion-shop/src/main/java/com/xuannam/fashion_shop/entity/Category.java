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
    private Long id;

    @NotNull
    @jakarta.validation.constraints.Size(max = 50)
    private Size name;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    int level;

}
