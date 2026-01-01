# 数据库设计

本篇文档详细阐述了电商微服务平台中各个微服务所使用的数据库表结构设计。我们选择 **PostgreSQL** 作为主要的关系型数据库，因为它功能强大、稳定可靠，并且拥有丰富的扩展性。

## 1. 设计原则

- **微服务独立数据库**: 每个微服务都拥有自己独立的数据库，以实现数据的隔离和服务的解耦。
- **领域驱动设计 (DDD)**: 数据库的设计遵循领域驱动设计的思想，以业务领域为核心，将相关的数据聚合在同一个微服务中。
- **数据一致性**: 在微服务架构中，我们通过事件驱动和最终一致性来保证跨服务的数据一致性。
- **索引优化**: 为常用的查询字段创建索引，以提高查询性能。

## 2. 用户服务 (user-service)

### 2.1 `users` 表

该表用于存储用户的基本信息，包括用户名、密码、邮箱、手机号等。

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,                -- 用户 ID，主键
    username VARCHAR(50) UNIQUE NOT NULL,    -- 用户名，唯一且不为空
    email VARCHAR(100) UNIQUE NOT NULL,      -- 邮箱，唯一且不为空
    password_hash VARCHAR(255) NOT NULL,     -- 加密后的密码
    phone VARCHAR(20),                       -- 手机号
    real_name VARCHAR(100),                  -- 真实姓名
    avatar_url VARCHAR(255),                 -- 头像 URL
    status VARCHAR(20) DEFAULT 'active',     -- 用户状态 (active, inactive, banned)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted_at TIMESTAMP                       -- 删除时间 (软删除)
);

-- 索引
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);
```

## 3. 商品服务 (product-service)

### 3.1 `products` 表

该表用于存储商品的信息，包括商品名称、描述、价格、库存等。

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,                -- 商品 ID，主键
    name VARCHAR(255) NOT NULL,              -- 商品名称
    description TEXT,                        -- 商品描述
    category_id BIGINT NOT NULL,             -- 分类 ID
    price DECIMAL(10, 2) NOT NULL,           -- 价格
    stock_quantity INT DEFAULT 0,            -- 库存数量
    status VARCHAR(20) DEFAULT 'active',     -- 商品状态 (active, inactive, sold_out)
    image_url VARCHAR(255),                  -- 商品图片 URL
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted_at TIMESTAMP                       -- 删除时间 (软删除)
);

-- 索引
CREATE INDEX idx_products_category_id ON products(category_id);
CREATE INDEX idx_products_status ON products(status);
CREATE INDEX idx_products_name ON products(name);
```

## 4. 订单服务 (order-service)

### 4.1 `orders` 表

该表用于存储订单的基本信息，包括订单号、用户 ID、总金额、订单状态等。

```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,                -- 订单 ID，主键
    order_number VARCHAR(50) UNIQUE NOT NULL, -- 订单号，唯一且不为空
    user_id BIGINT NOT NULL,                 -- 用户 ID
    total_amount DECIMAL(10, 2) NOT NULL,    -- 订单总金额
    status VARCHAR(20) DEFAULT 'pending',    -- 订单状态 (pending, paid, shipped, completed, cancelled)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted_at TIMESTAMP                       -- 删除时间 (软删除)
);

-- 索引
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_order_number ON orders(order_number);
```

### 4.2 `order_items` 表

该表用于存储订单中的商品信息。

```sql
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,                -- 订单项 ID，主键
    order_id BIGINT NOT NULL,                -- 订单 ID
    product_id BIGINT NOT NULL,              -- 商品 ID
    quantity INT NOT NULL,                   -- 商品数量
    price DECIMAL(10, 2) NOT NULL,           -- 商品单价
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 索引
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);
```

## 5. 支付服务 (payment-service)

### 5.1 `payments` 表

该表用于存储支付信息。

```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,                -- 支付 ID，主键
    order_id BIGINT NOT NULL,                -- 订单 ID
    amount DECIMAL(10, 2) NOT NULL,          -- 支付金额
    payment_method VARCHAR(50),              -- 支付方式 (alipay, wechat_pay)
    status VARCHAR(20) DEFAULT 'pending',    -- 支付状态 (pending, success, failed)
    transaction_id VARCHAR(100),             -- 第三方支付交易号
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 更新时间
    deleted_at TIMESTAMP                       -- 删除时间 (软删除)
);

-- 索引
CREATE INDEX idx_payments_order_id ON payments(order_id);
CREATE INDEX idx_payments_status ON payments(status);
```

## 6. 库存服务 (inventory-service)

### 6.1 `inventory` 表

该表用于存储商品的库存信息。

```sql
CREATE TABLE inventory (
    id BIGSERIAL PRIMARY KEY,                -- 库存 ID，主键
    product_id BIGINT NOT NULL,              -- 商品 ID
    quantity INT NOT NULL DEFAULT 0,         -- 可用库存
    reserved INT NOT NULL DEFAULT 0,         -- 预留库存
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- 更新时间
);

-- 索引
CREATE INDEX idx_inventory_product_id ON inventory(product_id);
```
