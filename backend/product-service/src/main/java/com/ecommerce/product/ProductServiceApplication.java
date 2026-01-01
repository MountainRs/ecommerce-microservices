package com.ecommerce.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品服务启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.ecommerce"})
public class ProductServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class, args);
    }
}
