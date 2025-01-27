package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
/*UserDetailsService là interface mặc định của Spring Security, được dùng để
* tải thông tin người dùng dựa trên username*/
public class CustomerUserServiceImplementation implements UserDetailsService {
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email - " + username);
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        /*Trả về org.springframework.security.core.userdetails.User để sử dụng cho việc xác thực
        * ở các lớp/hàm/service khác ...*/
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
