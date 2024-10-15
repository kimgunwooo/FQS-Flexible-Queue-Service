package com.f4.fqs.gateway.config;

import com.f4.fqs.gateway.application.response.UserDto;
//import com.f4.fqs.gateway.application.service.RedisService;
import com.f4.fqs.gateway.application.service.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebFluxSecurity
public class SecurityConfig {
    @Value("${service.jwt.secret-key}")
    private String secretKey;

    private final RedisService redisService;

    private List<String> WHITE_LIST = List.of(
            "/auth/signup", //회원가입
            "/auth/login/root", //로그인
            "/auth/login/iam", //로그인
            "/routes/refresh-routes",
            "/temporary-queue/queue/add",
            "/temporary-queue/queue/consume",
            "/temporary-queue/queue/ranks"
    );

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(jwtAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC); // csrf 비활성화

        return http.build();
    }


    public WebFilter jwtAuthenticationFilter() {
        //  jwt 인증 처리 필터
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();

            if(WHITE_LIST.stream().anyMatch(i -> i.equals(path))) {
                log.info("Skipping filter for path: {}", path);
                return chain.filter(exchange);
            }

            HttpHeaders headers = exchange.getRequest().getHeaders();

            String authHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
            String secretKeyHeader = headers.getFirst("secretKey");

            log.info("Authorization header: {}", authHeader); // 로그 추가
            log.info("secretkey header: {}", secretKeyHeader); // 로그 추가

            // 조건: secretKey가 있을 경우 JWT 검증을 생략
            if (!Objects.isEmpty(secretKeyHeader)) {
                log.info("Secret key is provided, skipping JWT validation");
                return chain.filter(exchange);  // JWT 검증을 건너뜀
            }

            // JWT 검증 필요
            if (!Objects.isEmpty(authHeader) && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                log.info("Extracted token: {}", token); // 로그 추가

                try {
                    byte[] bytes = Base64.getDecoder().decode(secretKey);
                    var secretKey = Keys.hmacShaKeyFor(bytes);

                    log.info("Decoded secret key bytes length: {}", bytes.length); // 로그 추가

                    Claims claims = Jwts
                            .parser()
                            .setSigningKey(secretKey)
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    log.info("Claims: {}", claims); // 로그 추가

                    // JWT 토큰 만료 검증
                    if (claims.getExpiration().before(new Date())) {
                        log.error("JWT token has expired");
                        return Mono.error(new RuntimeException("JWT Token is expired"));
                    }

                    Long userId = Long.valueOf(claims.get("id").toString());
                    String role = claims.get("role").toString();
                    log.info("checked user: {}[{}]", userId, role); // 로그 추가

                    var key = "user:" + userId;
                    var userDto = redisService.getValueAsClass(key, UserDto.class);

                    if (userDto == null) {
                        log.error("No user data found for key: {}", secretKey);
                    } else {
                        log.info("User data retrieved: {}", userDto);
                    }

                    /*var finalUserDto = Optional.ofNullable(
                            redisService.getValueAsClass("user:" + userId, UserDto.class)
                    ).orElseThrow(() -> new UsernameNotFoundException("User " + userId + " not found"));
                    */

                    // 사용자 정보를 새로운 헤더에 추가
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-User-Id", String.valueOf(userId))
//                            .header("X-User-SecretKey", String.valueOf(secretKey))
                            .header("X-User-Roles", role)
                            .build();

                    // 수정된 요청으로 필터 체인 계속 처리
                    ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

                    return chain.filter(modifiedExchange);

                } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
                    log.error("JWT validation error: {}", e.getMessage(), e); // 로그 추가
                    return Mono.error(new RuntimeException("Invalid JWT Token"));
                }
            }

            throw new RuntimeException("올바르지 않는 요청입니다");

        };
    }
}
