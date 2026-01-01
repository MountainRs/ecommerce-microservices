# 电商微服务平台 - 项目架构设计

## 1. 项目概述

本项目是一个完整的电商微服务平台，旨在展示现代互联网公司的技术栈和最佳实践。通过该项目，你将学习到从本地开发、容器化、微服务架构、到生产级部署的完整流程。

## 2. 系统架构

### 2.1 整体架构图

```
┌─────────────────────────────────────────────────────────────────┐
│                        客户端层                                   │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐           │
│  │  Web浏览器   │  │  移动应用    │  │  第三方系统  │           │
│  └──────┬───────┘  └──────┬───────┘  └──────┬───────┘           │
└─────────┼──────────────────┼──────────────────┼──────────────────┘
          │                  │                  │
┌─────────▼──────────────────▼──────────────────▼──────────────────┐
│                      Nginx 反向代理                               │
│  - 负载均衡                                                       │
│  - SSL/TLS 终止                                                   │
│  - 静态资源缓存                                                   │
└─────────┬──────────────────────────────────────────────────────┘
          │
┌─────────▼──────────────────────────────────────────────────────┐
│                    API 网关（Spring Cloud Gateway）             │
│  - 请求路由                                                     │
│  - 限流控制                                                     │
│  - 认证授权                                                     │
└─────────┬──────────────────────────────────────────────────────┘
          │
    ┌─────┴──────────────────┬──────────────────┬─────────────┐
    │                        │                  │             │
┌───▼────┐  ┌───────────┐ ┌──▼──────┐  ┌──────▼──┐  ┌──────▼──┐
│ 用户   │  │ 商品      │ │ 订单    │  │ 支付    │  │ 库存    │
│ 服务   │  │ 服务      │ │ 服务    │  │ 服务    │  │ 服务    │
└───┬────┘  └───┬───────┘ └──┬──────┘  └──┬─────┘  └──┬──────┘
    │           │            │           │           │
    └───────────┼────────────┼───────────┼───────────┘
                │            │           │
        ┌───────▼────────────▼───────────▼────────┐
        │     消息队列（RabbitMQ）                 │
        │  - 异步处理                              │
        │  - 服务解耦                              │
        │  - 事件驱动                              │
        └───────┬────────────────────────────────┘
                │
    ┌───────────┼───────────┬──────────────┐
    │           │           │              │
┌───▼──┐  ┌────▼───┐  ┌────▼──┐  ┌──────▼──┐
│ 数据 │  │ 缓存   │  │ 日志  │  │ 监控   │
│ 库   │  │ Redis  │  │ ELK   │  │ 系统   │
│ PG  │  │        │  │       │  │        │
└──────┘  └────────┘  └───────┘  └────────┘
```

### 2.2 微服务清单

| 服务名称 | 功能描述 | 技术栈 | 数据库 |
|---------|--------|------|------|
| user-service | 用户管理、认证、授权 | Spring Boot + JWT | PostgreSQL |
| product-service | 商品管理、分类、搜索 | Spring Boot + Elasticsearch | PostgreSQL |
| order-service | 订单管理、流程控制 | Spring Boot + Saga | PostgreSQL |
| payment-service | 支付处理、订单支付 | Spring Boot | PostgreSQL |
| inventory-service | 库存管理、扣减 | Spring Boot | PostgreSQL |
| gateway-service | API网关 | Spring Cloud Gateway | 无 |

## 3. 技术选型

### 3.1 后端技术栈

| 组件 | 技术 | 版本 | 用途 |
|-----|-----|-----|-----|
| 框架 | Spring Boot | 3.1.x | 微服务基础框架 |
| 服务治理 | Spring Cloud Alibaba | 2022.x | 注册发现、配置中心 |
| API网关 | Spring Cloud Gateway | 4.0.x | 请求路由、限流 |
| 数据库 | PostgreSQL | 14+ | 关系型数据存储 |
| 缓存 | Redis | 7.0+ | 分布式缓存 |
| 消息队列 | RabbitMQ | 3.12+ | 异步消息处理 |
| 搜索引擎 | Elasticsearch | 8.0+ | 全文搜索、日志分析 |
| 日志 | ELK Stack | 8.0+ | 日志聚合、分析 |
| 监控 | Prometheus + Grafana | 最新 | 性能监控、告警 |
| 容器化 | Docker | 最新 | 应用容器化 |
| 编排 | Kubernetes | 1.27+ | 容器编排、部署 |

### 3.2 前端技术栈

| 组件 | 技术 | 版本 | 用途 |
|-----|-----|-----|-----|
| 框架 | Vue | 3.x | 前端框架 |
| 语言 | TypeScript | 5.x | 类型安全 |
| 构建工具 | Vite | 5.x | 快速构建 |
| UI组件库 | Element Plus | 2.x | UI组件 |
| 状态管理 | Pinia | 2.x | 状态管理 |
| HTTP客户端 | Axios | 1.x | HTTP请求 |
| 路由 | Vue Router | 4.x | 前端路由 |
| 样式 | Tailwind CSS | 3.x | 原子化CSS |

### 3.3 DevOps技术栈

| 组件 | 技术 | 用途 |
|-----|-----|-----|
| 版本控制 | Git + GitHub | 代码管理 |
| CI/CD | GitHub Actions | 自动化构建部署 |
| 容器镜像 | Docker | 应用容器化 |
| 镜像仓库 | Docker Hub | 镜像存储 |
| 容器编排 | Kubernetes | 生产部署 |
| 反向代理 | Nginx | 负载均衡 |
| 配置管理 | Nacos | 配置中心 |

## 4. 项目结构

```
ecommerce-microservices/
├── docs/                          # 文档目录
│   ├── architecture.md            # 架构设计文档
│   ├── api-design.md              # API设计规范
│   ├── database-design.md         # 数据库设计
│   ├── git-workflow.md            # Git工作流指南
│   ├── deployment-guide.md        # 部署指南
│   ├── development-guide.md       # 开发指南
│   └── diagrams/                  # 架构图
│
├── backend/                       # 后端代码
│   ├── pom.xml                    # Maven 父POM
│   ├── gateway-service/           # API网关
│   ├── user-service/              # 用户服务
│   ├── product-service/           # 商品服务
│   ├── order-service/             # 订单服务
│   ├── payment-service/           # 支付服务
│   ├── inventory-service/         # 库存服务
│   └── common/                    # 公共模块
│
├── frontend/                      # 前端代码
│   ├── src/
│   │   ├── components/            # Vue组件
│   │   ├── pages/                 # 页面
│   │   ├── stores/                # Pinia状态管理
│   │   ├── api/                   # API调用
│   │   ├── router/                # 路由配置
│   │   └── App.vue                # 根组件
│   ├── package.json
│   └── vite.config.ts
│
├── docker/                        # Docker配置
│   ├── Dockerfile.backend         # 后端Dockerfile
│   ├── Dockerfile.frontend        # 前端Dockerfile
│   └── docker-compose.yml         # Docker Compose配置
│
├── kubernetes/                    # Kubernetes配置
│   ├── namespaces.yaml            # 命名空间
│   ├── configmaps.yaml            # 配置映射
│   ├── secrets.yaml               # 密钥
│   ├── deployments/               # 部署配置
│   ├── services/                  # 服务配置
│   ├── ingress.yaml               # Ingress配置
│   └── persistent-volumes.yaml    # 持久化存储
│
├── nginx/                         # Nginx配置
│   ├── nginx.conf                 # 主配置文件
│   ├── conf.d/                    # 虚拟主机配置
│   └── ssl/                       # SSL证书
│
├── scripts/                       # 脚本文件
│   ├── build.sh                   # 构建脚本
│   ├── deploy.sh                  # 部署脚本
│   ├── init-db.sh                 # 数据库初始化
│   └── health-check.sh            # 健康检查
│
├── .github/                       # GitHub配置
│   └── workflows/                 # CI/CD工作流
│       ├── build.yml              # 构建工作流
│       └── deploy.yml             # 部署工作流
│
├── .gitignore
├── README.md
└── LICENSE
```

## 5. 数据库设计

### 5.1 用户表 (users)

```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    real_name VARCHAR(100),
    avatar_url VARCHAR(255),
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);
```

### 5.2 商品表 (products)

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);
```

### 5.3 订单表 (orders)

```sql
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP
);
```

## 6. 认证与授权

### 6.1 JWT 无状态认证

- 使用 JWT Token 进行用户认证
- Token 包含用户ID、用户名、权限等信息
- 每个请求在 Authorization Header 中携带 Token
- 服务端验证 Token 的有效性和签名

### 6.2 权限控制

- 基于角色的访问控制 (RBAC)
- 用户、管理员、超级管理员三种角色
- 通过注解 @PreAuthorize 实现方法级权限控制

## 7. 部署架构

### 7.1 开发环境

- 本地 Windows 开发机
- Docker Desktop 运行容器
- Docker Compose 编排多个服务

### 7.2 测试环境

- Linux 服务器
- Docker 容器化部署
- Nginx 反向代理

### 7.3 生产环境

- Kubernetes 集群
- 多副本部署
- 自动扩展、自我修复
- 蓝绿部署策略

## 8. Git 工作流

### 8.1 分支策略 (Git Flow)

- **main**: 生产环境分支，仅接收来自 release 分支的合并
- **develop**: 开发分支，集成各个功能分支
- **feature/**: 功能分支，从 develop 创建，完成后合并回 develop
- **release/**: 发布分支，用于发布前的准备
- **hotfix/**: 热修复分支，用于生产环境的紧急修复

### 8.2 提交规范

遵循 Conventional Commits 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

- **type**: feat, fix, docs, style, refactor, perf, test, chore
- **scope**: 影响的模块或服务
- **subject**: 简短描述
- **body**: 详细描述（可选）
- **footer**: 关联的 Issue（可选）

## 9. 学习路径

### 第一阶段：基础搭建
1. 理解微服务架构
2. 搭建本地开发环境
3. 创建基础服务框架

### 第二阶段：核心功能
1. 实现用户认证系统
2. 实现商品管理服务
3. 实现订单管理服务

### 第三阶段：高级特性
1. 实现分布式事务
2. 实现缓存策略
3. 实现消息队列

### 第四阶段：容器化与部署
1. 编写 Dockerfile
2. 配置 Kubernetes
3. 配置 Nginx 和 CI/CD

### 第五阶段：监控与运维
1. 集成监控系统
2. 配置日志聚合
3. 实现健康检查

## 10. 下一步

本文档为项目的整体架构设计。后续将详细介绍：
- API 设计规范
- 数据库详细设计
- 开发环境搭建指南
- Git 工作流实践指南
- Docker 和 Kubernetes 部署指南
