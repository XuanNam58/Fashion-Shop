package com.xuannam.fashion_shop.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        if (jwt != null) {
            jwt = jwt.substring(7);
            try {
//                Keys.hmacShaKeyFor: Sử dụng thuật toán HMAC-SHA để tạo khóa bí mật từ chuỗi byte của SECRET_KEY
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

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
                throw new BadCredentialsException("Invalid token... from jwt validator");
            }
        }

        filterChain.doFilter(request, response);
    }
}
