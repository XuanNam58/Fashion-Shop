package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.configuration.JwtProvider;
import com.xuannam.fashion_shop.dto.response.AuthResponse;
import com.xuannam.fashion_shop.dto.resquest.LoginRequest;
import com.xuannam.fashion_shop.dto.resquest.RefreshTokenRequest;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.repository.UserRepository;
import com.xuannam.fashion_shop.service.RefreshTokenService;
import com.xuannam.fashion_shop.service.TokenBlacklistService;
import com.xuannam.fashion_shop.service.impl.CustomerUserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Auth Controller")
public class AuthController {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    CustomerUserServiceImpl customerUserServiceImplementation;
    RefreshTokenService refreshTokenService;
    TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
    @Operation(summary = "Signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user, HttpServletResponse response) throws UserException {
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
                .role("USER")
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), password);
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        // Lưu refresh token vào cookie
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(refreshTokenCookie);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(accessToken)
                .message("Signup Success")
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    @Operation(summary = "Login")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest,
                                                         HttpServletResponse response) throws UserException {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password));
        } catch (BadCredentialsException e) {
            throw new UserException("Invalid email or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(authentication);

        refreshTokenService.saveRefreshToken(refreshToken, email);

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7d
        response.addCookie(refreshTokenCookie);
//        refreshToken được lưu trong cookie HttpOnly và Secure để tăng bảo mật (không thể truy cập qua JavaScript,
//        chỉ server-side hoặc HTTPS có thể đọc).

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(accessToken)
                .message("Signin Success")
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token")
    public ResponseEntity<AuthResponse> refreshTokenHandler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return new ResponseEntity<>(AuthResponse.builder()
                    .message("Refresh token is required")
                    .build(), HttpStatus.BAD_REQUEST);
        }

        if (tokenBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new IllegalArgumentException("Refresh token has been revoked");
        }

        if (!refreshTokenService.isTokenValid(refreshToken) || !jwtProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String email = jwtProvider.getEmailFromToken(refreshToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null,
                customerUserServiceImplementation.loadUserByUsername(email).getAuthorities());
        String newAccessToken = jwtProvider.generateToken(authentication);
        String newRefreshToken = jwtProvider.generateRefreshToken(authentication);

        tokenBlacklistService.blacklistToken(refreshToken, "Revoked due to refresh", 604800); // 7 ngày
        refreshTokenService.revokeToken(refreshToken);
        refreshTokenService.saveRefreshToken(newRefreshToken, email);

        Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        newRefreshTokenCookie.setHttpOnly(true);
        newRefreshTokenCookie.setSecure(true);
        newRefreshTokenCookie.setPath("/");
        newRefreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
        response.addCookie(newRefreshTokenCookie);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(newAccessToken)
                .message("Token refreshed successfully")
                .build();
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout")
    public ResponseEntity<String> logoutHandler(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return new ResponseEntity<>("Refresh token is required", HttpStatus.BAD_REQUEST);
        }

        tokenBlacklistService.blacklistToken(refreshToken, "Revoked due to logout", 604800); // 7 ngày
        refreshTokenService.revokeToken(refreshToken);
        // Xóa cookie refreshToken (nếu cần)
        Cookie deleteCookie = new Cookie("refreshToken", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);
        return new ResponseEntity<>("Logout successful", HttpStatus.OK);
    }

}
