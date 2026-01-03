package com.ecommerce.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.security.web.SecurityFilterChain;
import java.util.Arrays;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
/**
 * Spring Security 配置
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // 禁用 CSRF（开发环境可以关闭）
//                .csrf(csrf -> csrf.disable())
//
//                // 配置 CORS
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//
//                // 配置请求授权
//                .authorizeHttpRequests(authz -> authz
//                        // 放行注册接口和静态资源
//                        .requestMatchers(
//                                "/api/user/register",  // 注册接口
//                                "/api/user/login",     // 登录接口（如果有的话）
//                                "/error",              // 错误页面
//                                "/actuator/**"         // 监控端点（根据你的配置）
//                        ).permitAll()
//
//                        // 其他所有请求需要认证
//                        .anyRequest().authenticated()
//                )
//
//                // 禁用 Basic 认证（除非你需要）
//                .httpBasic(httpBasic -> httpBasic.disable());
//
//        return http.build();
//    }

    // CORS 配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost",
                "http://localhost:5173",
                "http://localhost:8081"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
