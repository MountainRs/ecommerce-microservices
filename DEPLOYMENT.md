# 电商微服务项目构建和部署指南

## 项目概述

这是一个基于Spring Boot微服务架构和Vue.js前端的电商平台项目。

## 项目结构

- `backend/` - 后端微服务
  - `common/` - 公共模块
  - `user-service/` - 用户服务
  - `product-service/` - 商品服务
- `frontend/` - Vue.js前端应用
- `docker/` - Docker相关配置
- `scripts/` - 构建和部署脚本

## 本地开发环境设置

### 前置条件

- Node.js 20+
- Java 17+
- Maven 3.9+
- Docker & Docker Compose
- pnpm

### 前端开发

```bash
cd frontend
pnpm install
pnpm dev
```

### 后端开发

```bash
cd backend
mvn clean install
```

### 本地Docker环境

```bash
# 启动基础服务（PostgreSQL和Redis）
docker-compose -f docker/docker-compose-simple.yml up -d postgres redis

# 启动后端服务
mvn spring-boot:run -pl user-service
mvn spring-boot:run -pl product-service

# 启动前端
cd frontend
pnpm dev
```

## 生产环境部署

### 快速部署

1. 克隆项目到云服务器
2. 运行构建脚本：

```bash
chmod +x scripts/build-and-deploy.sh
./scripts/build-and-deploy.sh
```

### 手动部署步骤

1. 构建前端镜像：
```bash
docker build -f docker/Dockerfile.frontend -t ecommerce-frontend .
```

2. 构建后端服务镜像：
```bash
# 用户服务
docker build -f docker/Dockerfile.backend.new --build-arg SERVICE_NAME=user-service -t ecommerce-user-service .

# 商品服务
docker build -f docker/Dockerfile.backend.new --build-arg SERVICE_NAME=product-service -t ecommerce-product-service .
```

3. 启动所有服务：
```bash
docker-compose -f docker/docker-compose-simple.yml up -d
```

## 服务端口

- 前端: 80
- 用户服务: 8081
- 商品服务: 8082
- PostgreSQL: 5432
- Redis: 6379

## API端点

- 用户服务: `/api/user/*`
- 商品服务: `/api/product/*`

## 健康检查

- 用户服务: `http://localhost:8081/actuator/health`
- 商品服务: `http://localhost:8082/actuator/health`

## 故障排除

1. 如果遇到端口冲突，请检查端口是否被占用
2. 如果数据库连接失败，请检查PostgreSQL服务是否正常运行
3. 如果前端无法访问后端API，请检查nginx配置和网络连接

## 查看日志

```bash
# 查看所有服务日志
docker-compose -f docker/docker-compose-simple.yml logs -f

# 查看特定服务日志
docker-compose -f docker/docker-compose-simple.yml logs -f user-service
```