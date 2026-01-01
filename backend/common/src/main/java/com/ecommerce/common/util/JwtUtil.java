package com.ecommerce.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret:ecommerce-microservices-secret-key-for-jwt-token-generation-and-validation}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    /**
     * 生成 JWT Token
     */
    public String generateToken(Long userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, String.valueOf(userId));
    }
    
    /**
     * 生成 JWT Token（带自定义声明）
     */
    public String generateToken(Long userId, String username, Map<String, Object> customClaims) {
        Map<String, Object> claims = new HashMap<>(customClaims);
        claims.put("userId", userId);
        claims.put("username", username);
        return createToken(claims, String.valueOf(userId));
    }
    
    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
    
    /**
     * 验证 Token
     */
    public Boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Token 验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 从 Token 中获取用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("userId", Long.class);
    }
    
    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("username", String.class);
    }
    
    /**
     * 从 Token 中获取所有声明
     */
    private Claims getAllClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * 检查 Token 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
}
