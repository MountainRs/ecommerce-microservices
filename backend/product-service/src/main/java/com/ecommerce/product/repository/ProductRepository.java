package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 商品仓储接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 根据分类 ID 查询商品
     */
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    
    /**
     * 根据名称模糊查询商品
     */
    Page<Product> findByNameContaining(String name, Pageable pageable);
    
    /**
     * 根据状态查询商品
     */
    Page<Product> findByStatus(String status, Pageable pageable);
}
