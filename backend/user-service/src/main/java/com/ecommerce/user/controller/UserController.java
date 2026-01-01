package com.ecommerce.user.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.util.JwtUtil;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> register(@RequestBody RegisterRequest request) {
        log.info("用户注册请求: {}", request.getUsername());
        UserDto userDto = userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("注册成功", userDto));
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("用户登录请求: {}", request.getUsername());
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(ApiResponse.success("登录成功", response));
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(ApiResponse.fail(401, "未授权"));
        }
        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.ok(ApiResponse.fail(401, "Token 无效"));
        }
        
        Long userId = jwtUtil.getUserIdFromToken(token);
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }
    
    /**
     * 根据 ID 获取用户信息
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success(userDto));
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserDto userDto,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(ApiResponse.fail(401, "未授权"));
        }
        
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.ok(ApiResponse.fail(401, "Token 无效"));
        }
        
        Long currentUserId = jwtUtil.getUserIdFromToken(token);
        if (!currentUserId.equals(userId)) {
            return ResponseEntity.ok(ApiResponse.fail(403, "无权限修改他人信息"));
        }
        
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updatedUser));
    }
}
