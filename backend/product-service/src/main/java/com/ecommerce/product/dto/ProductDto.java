package com.ecommerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    
    /**
     * 商品 ID
     */
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 分类 ID
     */
    private Long categoryId;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 库存数量
     */
    private Integer stockQuantity;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 图片 URL
     */
    private String imageUrl;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
