package com.ecommerce.product.service.impl;

import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品服务实现
 */
@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .categoryId(productDto.getCategoryId())
                .price(productDto.getPrice())
                .stockQuantity(productDto.getStockQuantity() != null ? productDto.getStockQuantity() : 0)
                .imageUrl(productDto.getImageUrl())
                .build();
        
        product = productRepository.save(product);
        log.info("商品创建成功: {}", product.getName());
        
        return convertToDto(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        return convertToDto(product);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDto> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable)
                .map(this::convertToDto);
    }
    
    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        
        if (productDto.getName() != null) {
            product.setName(productDto.getName());
        }
        if (productDto.getDescription() != null) {
            product.setDescription(productDto.getDescription());
        }
        if (productDto.getPrice() != null) {
            product.setPrice(productDto.getPrice());
        }
        if (productDto.getImageUrl() != null) {
            product.setImageUrl(productDto.getImageUrl());
        }
        if (productDto.getStatus() != null) {
            product.setStatus(productDto.getStatus());
        }
        
        product = productRepository.save(product);
        log.info("商品更新成功: {}", product.getName());
        
        return convertToDto(product);
    }
    
    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        
        productRepository.delete(product);
        log.info("商品删除成功: {}", product.getName());
    }
    
    @Override
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(404, "商品不存在"));
        
        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new BusinessException(400, "库存不足");
        }
        
        product.setStockQuantity(newStock);
        productRepository.save(product);
        log.info("商品库存更新: {}, 新库存: {}", product.getName(), newStock);
    }
    
    /**
     * 将商品实体转换为 DTO
     */
    private ProductDto convertToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategoryId())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .status(product.getStatus())
                .imageUrl(product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
