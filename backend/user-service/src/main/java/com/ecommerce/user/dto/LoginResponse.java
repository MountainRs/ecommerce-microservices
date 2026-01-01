package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    
    /**
     * 用户 ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * Token 类型
     */
    private String tokenType;
    
    /**
     * 过期时间（毫秒）
     */
    private Long expiresIn;
}
