package com.xuannam.fashion_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "first_name")
    Size firstName;

    @Column(name = "last_name")
    Size lastName;

    @Column(name = "street_address")
    Size streetAddress;

    @Column(name = "city")
    Size city;

    @Column(name = "state")
    Size state;

    @Column(name = "zip_code")
    Size zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    Size mobile;

}
