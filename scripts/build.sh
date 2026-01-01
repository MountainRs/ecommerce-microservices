#!/bin/bash

# 电商微服务平台 - 构建脚本

set -e

echo "=========================================="
echo "电商微服务平台 - 构建"
echo "=========================================="

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo ""
echo "📁 项目目录: $PROJECT_DIR"

# 进入项目目录
cd "$PROJECT_DIR"

# 构建后端
echo ""
echo "🔨 构建后端项目..."
cd backend

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven 未安装，请先安装 Maven"
    exit 1
fi

mvn clean package -DskipTests -q

echo "✅ 后端构建成功"

# 返回项目根目录
cd "$PROJECT_DIR"

# 构建前端
echo ""
echo "🔨 构建前端项目..."
cd frontend

# 检查 Node.js 是否安装
if ! command -v node &> /dev/null; then
    echo "❌ Node.js 未安装，请先安装 Node.js"
    exit 1
fi

# 检查 npm 是否安装
if ! command -v npm &> /dev/null; then
    echo "❌ npm 未安装，请先安装 npm"
    exit 1
fi

# 安装依赖
if [ ! -d "node_modules" ]; then
    echo "📦 安装前端依赖..."
    npm install
fi

# 构建
npm run build

echo "✅ 前端构建成功"

# 返回项目根目录
cd "$PROJECT_DIR"

echo ""
echo "=========================================="
echo "✅ 构建完成！"
echo "=========================================="
echo ""
echo "📦 构建产物:"
echo "  - 后端 JAR: backend/*/target/*.jar"
echo "  - 前端: frontend/dist/"
echo ""
