package com.ecommerce.user.service.impl;

import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.util.JwtUtil;
import com.ecommerce.user.dto.LoginRequest;
import com.ecommerce.user.dto.LoginResponse;
import com.ecommerce.user.dto.RegisterRequest;
import com.ecommerce.user.dto.UserDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务实现
 */
@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    @Override
    public UserDto register(RegisterRequest request) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(400, "邮箱已存在");
        }
        
        // 检查密码是否一致
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException(400, "两次输入的密码不一致");
        }
        
        // 创建用户
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .realName(request.getRealName())
                .build();
        
        user = userRepository.save(user);
        log.info("用户注册成功: {}", user.getUsername());
        
        return convertToDto(user);
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        // 查询用户
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(401, "用户名或密码错误"));
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        
        // 检查用户状态
        if (!"active".equals(user.getStatus())) {
            throw new BusinessException(403, "用户已被禁用");
        }
        
        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        log.info("用户登录成功: {}", user.getUsername());
        
        return LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiration)
                .build();
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = getUserEntityById(userId);
        return convertToDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
        return convertToDto(user);
    }
    
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        User user = getUserEntityById(userId);
        
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getRealName() != null) {
            user.setRealName(userDto.getRealName());
        }
        if (userDto.getAvatarUrl() != null) {
            user.setAvatarUrl(userDto.getAvatarUrl());
        }
        
        user = userRepository.save(user);
        log.info("用户信息更新成功: {}", user.getUsername());
        
        return convertToDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public User getUserEntityById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));
    }
    
    /**
     * 将用户实体转换为 DTO
     */
    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .realName(user.getRealName())
                .avatarUrl(user.getAvatarUrl())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
