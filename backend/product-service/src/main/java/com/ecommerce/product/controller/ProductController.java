package com.ecommerce.product.controller;

import com.ecommerce.common.dto.ApiResponse;
import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 创建商品
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductDto productDto) {
        log.info("创建商品: {}", productDto.getName());
        ProductDto created = productService.createProduct(productDto);
        return ResponseEntity.ok(ApiResponse.success("创建成功", created));
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> getProduct(@PathVariable Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        return ResponseEntity.ok(ApiResponse.success(productDto));
    }
    
    /**
     * 分页查询所有商品
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    /**
     * 根据分类查询商品
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getProductsByCategory(categoryId, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductDto>>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.searchProducts(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(products));
    }
    
    /**
     * 更新商品
     */
    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDto productDto) {
        log.info("更新商品: {}", productId);
        ProductDto updated = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(ApiResponse.success("更新成功", updated));
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        log.info("删除商品: {}", productId);
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success("删除成功", null));
    }
    
    /**
     * 更新库存
     */
    @PutMapping("/{productId}/stock")
    public ResponseEntity<ApiResponse<Void>> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        log.info("更新商品库存: {}, 数量: {}", productId, quantity);
        productService.updateStock(productId, quantity);
        return ResponseEntity.ok(ApiResponse.success("库存更新成功", null));
    }
}
