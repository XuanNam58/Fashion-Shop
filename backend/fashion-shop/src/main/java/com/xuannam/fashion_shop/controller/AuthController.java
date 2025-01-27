package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.configuration.JwtProvider;
import com.xuannam.fashion_shop.dto.response.AuthResponse;
import com.xuannam.fashion_shop.dto.resquest.LoginRequest;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.repository.UserRepository;
import com.xuannam.fashion_shop.service.CustomerUserServiceImplementation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {
    UserRepository userRepository;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    CustomerUserServiceImplementation customerUserServiceImplementation;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();

        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new UserException("Email is Already Used With Another Account");
        }

        User createdUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(token)
                .message("Signup Success")
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(token)
                .message("Signin Success")
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = customerUserServiceImplementation.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid Username...");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
