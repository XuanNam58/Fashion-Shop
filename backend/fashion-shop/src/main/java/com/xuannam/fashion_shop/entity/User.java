package com.xuannam.fashion_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String firstName;
    String lastName;
    String password;
    @Column(unique = true)
    String email;
    String role;
    String mobile;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Address> addresses = new ArrayList<>();

    @Embedded // có thể có hoặc ko
    @ElementCollection // Chỉ ra rằng danh sách paymentInformations là danh sách các giá trị nhúng (embedded), chứ không phải là entity độc lập.
    @CollectionTable(name = "payment_information", joinColumns = @JoinColumn(name = "user_id")) // Xác định tên bảng và khóa ngoại cho collection này. Ở đây, tạo bảng payment_information liên kết với entity chính thông qua user_id.
    List<PaymentInformation> paymentInformations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Rating> ratings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    List<Review> reviews = new ArrayList<>();
    LocalDateTime createdAt;

}
