package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 创建商品
     */
    ProductDto createProduct(ProductDto productDto);
    
    /**
     * 根据 ID 获取商品
     */
    ProductDto getProductById(Long productId);
    
    /**
     * 分页查询所有商品
     */
    Page<ProductDto> getAllProducts(Pageable pageable);
    
    /**
     * 根据分类 ID 查询商品
     */
    Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable);
    
    /**
     * 搜索商品
     */
    Page<ProductDto> searchProducts(String keyword, Pageable pageable);
    
    /**
     * 更新商品
     */
    ProductDto updateProduct(Long productId, ProductDto productDto);
    
    /**
     * 删除商品
     */
    void deleteProduct(Long productId);
    
    /**
     * 更新库存
     */
    void updateStock(Long productId, Integer quantity);
}
