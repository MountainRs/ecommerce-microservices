#!/bin/bash

# 电商微服务构建脚本
# 用于在云服务器上构建和部署整个应用

echo "开始构建电商微服务..."

# 检查Docker是否安装
if ! command -v docker &> /dev/null
then
    echo "Docker 未安装，请先安装 Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null
then
    echo "Docker Compose 未安装，请先安装 Docker Compose"
    exit 1
fi

# 停止并删除现有容器
echo "停止现有容器..."
docker-compose -f docker/docker-compose-simple.yml down --remove-orphans

# 清理未使用的镜像和容器
echo "清理Docker资源..."
docker system prune -f

# 构建前端镜像
echo "构建前端镜像..."
docker build -f docker/Dockerfile.frontend -t ecommerce-frontend .

# 构建用户服务镜像
echo "构建用户服务镜像..."
docker build -f docker/Dockerfile.backend.new --build-arg SERVICE_NAME=user-service -t ecommerce-user-service .

# 构建商品服务镜像
echo "构建商品服务镜像..."
docker build -f docker/Dockerfile.backend.new --build-arg SERVICE_NAME=product-service -t ecommerce-product-service .

# 启动所有服务
echo "启动所有服务..."
docker-compose -f docker/docker-compose-simple.yml up -d

# 检查服务状态
echo "检查服务状态..."
sleep 10
docker-compose -f docker/docker-compose-simple.yml ps

echo "构建完成！"
echo "前端地址: http://localhost"
echo "用户服务地址: http://localhost:8081"
echo "商品服务地址: http://localhost:8082"
echo "查看日志: docker-compose -f docker/docker-compose-simple.yml logs -f"