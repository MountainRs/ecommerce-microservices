#!/bin/bash

# 电商微服务平台 - Kubernetes 部署脚本

set -e

echo "=========================================="
echo "电商微服务平台 - Kubernetes 部署"
echo "=========================================="

# 检查 kubectl 是否安装
if ! command -v kubectl &> /dev/null; then
    echo "❌ kubectl 未安装，请先安装 kubectl"
    exit 1
fi

# 获取脚本所在目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"

echo ""
echo "📁 项目目录: $PROJECT_DIR"

# 进入项目目录
cd "$PROJECT_DIR"

# 检查 Kubernetes 集群连接
echo ""
echo "🔍 检查 Kubernetes 集群..."
if ! kubectl cluster-info &> /dev/null; then
    echo "❌ 无法连接到 Kubernetes 集群"
    exit 1
fi

echo "✅ Kubernetes 集群连接成功"

# 创建命名空间
echo ""
echo "📦 创建命名空间..."
kubectl apply -f kubernetes/namespace.yaml

# 创建 Secret
echo ""
echo "🔐 创建 Secret..."
kubectl create secret generic postgres-secret \
    --from-literal=POSTGRES_USER=postgres \
    --from-literal=POSTGRES_PASSWORD=postgres \
    -n ecommerce \
    --dry-run=client -o yaml | kubectl apply -f -

# 部署基础设施
echo ""
echo "🗄️  部署 PostgreSQL..."
kubectl apply -f kubernetes/postgres.yaml

echo ""
echo "💾 部署 Redis..."
kubectl apply -f kubernetes/redis.yaml

# 等待基础设施就绪
echo ""
echo "⏳ 等待基础设施就绪..."
kubectl wait --for=condition=ready pod -l app=postgres -n ecommerce --timeout=300s || true
kubectl wait --for=condition=ready pod -l app=redis -n ecommerce --timeout=300s || true

# 部署微服务
echo ""
echo "🚀 部署用户服务..."
kubectl apply -f kubernetes/user-service.yaml

echo ""
echo "🚀 部署商品服务..."
kubectl apply -f kubernetes/product-service.yaml

# 部署前端
echo ""
echo "🌐 部署前端应用..."
kubectl apply -f kubernetes/frontend.yaml

# 部署 Ingress
echo ""
echo "🔀 部署 Ingress..."
kubectl apply -f kubernetes/ingress.yaml

# 等待部署完成
echo ""
echo "⏳ 等待部署完成..."
kubectl rollout status deployment/user-service -n ecommerce --timeout=300s || true
kubectl rollout status deployment/product-service -n ecommerce --timeout=300s || true
kubectl rollout status deployment/frontend -n ecommerce --timeout=300s || true

# 显示部署状态
echo ""
echo "=========================================="
echo "✅ Kubernetes 部署完成！"
echo "=========================================="
echo ""
echo "📋 部署状态:"
kubectl get pods -n ecommerce
echo ""
echo "📋 服务地址:"
kubectl get svc -n ecommerce
echo ""
echo "💡 提示:"
echo "  - 查看 Pod 日志: kubectl logs -f <pod-name> -n ecommerce"
echo "  - 进入 Pod: kubectl exec -it <pod-name> -n ecommerce -- /bin/bash"
echo "  - 查看 Ingress: kubectl get ingress -n ecommerce"
echo "  - 删除部署: kubectl delete namespace ecommerce"
echo ""
