#!/bin/bash

# 电商微服务平台 - 本地开发启动脚本

set -e

echo "=========================================="
echo "电商微服务平台 - 本地开发环境启动"
echo "=========================================="

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ Docker 未安装，请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose 未安装，请先安装 Docker Compose"
    exit 1
fi

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo ""
echo "📁 项目目录: $PROJECT_DIR"

# 进入项目目录
cd "$PROJECT_DIR"

# 启动 Docker Compose
echo ""
echo "🚀 启动 Docker Compose 服务..."
docker-compose -f docker/docker-compose.yml up -d

# 等待服务启动
echo ""
echo "⏳ 等待服务启动..."
sleep 10

# 检查服务状态
echo ""
echo "✅ 检查服务状态..."
docker-compose -f docker/docker-compose.yml ps

echo ""
echo "=========================================="
echo "✅ 本地开发环境启动成功！"
echo "=========================================="
echo ""
echo "📋 服务地址:"
echo "  - PostgreSQL: localhost:5432"
echo "  - Redis: localhost:6379"
echo "  - RabbitMQ: localhost:5672 (管理界面: http://localhost:15672)"
echo "  - Nacos: http://localhost:8848/nacos"
echo "  - 用户服务: http://localhost:8081"
echo "  - 商品服务: http://localhost:8082"
echo "  - 前端应用: http://localhost"
echo ""
echo "💡 提示:"
echo "  - 查看日志: docker-compose -f docker/docker-compose.yml logs -f [service_name]"
echo "  - 停止服务: docker-compose -f docker/docker-compose.yml down"
echo "  - 清理数据: docker-compose -f docker/docker-compose.yml down -v"
echo ""
