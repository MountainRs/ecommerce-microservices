package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 确认密码
     */
    private String confirmPassword;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 真实姓名
     */
    private String realName;
}
