package com.ecommerce.user.service;

import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    UserDto register(RegisterRequest request);
    
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 根据 ID 获取用户
     */
    UserDto getUserById(Long userId);
    
    /**
     * 根据用户名获取用户
     */
    UserDto getUserByUsername(String username);
    
    /**
     * 更新用户信息
     */
    UserDto updateUser(Long userId, UserDto userDto);
    
    /**
     * 根据 ID 获取用户实体
     */
    User getUserEntityById(Long userId);
}
