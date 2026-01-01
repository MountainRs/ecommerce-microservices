package com.ecommerce.common.config;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(
            BusinessException ex, WebRequest request) {
        log.error("业务异常: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(ex.getCode() >= 500 ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.BAD_REQUEST)
                .body(ApiResponse.fail(ex.getCode(), ex.getMessage()));
    }
    
    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(
            Exception ex, WebRequest request) {
        log.error("系统异常: {}", ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(500, "系统内部错误"));
    }
}
