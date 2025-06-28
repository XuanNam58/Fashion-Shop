package com.xuannam.fashion_shop.configuration;

import com.xuannam.fashion_shop.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class JwtValidator extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    String secretKey;

    TokenBlacklistService tokenBlacklistService;
    String[] publicEndpoints;

    @Autowired
    public JwtValidator(TokenBlacklistService tokenBlacklistService, String[] publicEndpoints) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.publicEndpoints = publicEndpoints;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return Arrays.stream(publicEndpoints)
                .anyMatch(pattern -> new AntPathRequestMatcher(pattern).matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.getWriter().write("Token has been revoked");
                return;
            }

            try {
//                Keys.hmacShaKeyFor: Sử dụng thuật toán HMAC-SHA để tạo khóa bí mật từ chuỗi byte của SECRET_KEY
                SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

                /*Jwts.parserBuilder(): Tạo một đối tượng để phân tích JWT.
                 *parseClaimsJws(jwt): Xác thực token bằng khóa bí mật. Nếu token không hợp lệ (ví dụ: hết hạn, bị chỉnh sửa, hoặc ký không đúng),
                 *  phương thức này sẽ ném ra một ngoại lệ.
                 *getBody(): Trích xuất payload (các claims) của token sau khi xác thực thành công. */
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                /*claims.get("email"): Lấy thông tin email của người dùng từ payload của JWT.
                 *claims.get("authorities"): Lấy danh sách quyền hạn (authorities) từ JWT.
                 * Dữ liệu này thường được lưu dưới dạng chuỗi phân cách bởi dấu phẩy (ví dụ: "ROLE_USER,ROLE_ADMIN"). */

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                /*AuthorityUtils.commaSeparatedStringToAuthorityList(authorities):
                Chuyển chuỗi danh sách quyền hạn thành danh sách các đối tượng GrantedAuthority
                UsernamePasswordAuthenticationToken: Tạo một đối tượng Authentication chứa thông tin:
                    email: Dùng làm principal (đối tượng chính để xác thực).
                    null: Không có thông tin mật khẩu, vì JWT đã xác thực người dùng.
                    auths: Danh sách quyền hạn của người dùng.
*/
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                /*SecurityContextHolder: Lưu trữ thông tin xác thực cho request hiện tại.*/
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Access token expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
