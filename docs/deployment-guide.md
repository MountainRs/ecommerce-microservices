# 部署指南

本指南将详细介绍如何将电商微服务平台部署到不同的环境中，包括本地开发环境、基于 Docker 的生产环境以及基于 Kubernetes 的生产环境。

## 1. 环境要求

在开始部署之前，请确保你的环境中已经安装了以下软件：

- **Git**: 用于从 GitHub 克隆项目代码。
- **Docker**: 用于构建和运行容器化应用。
- **Docker Compose**: 用于在本地编排多个 Docker 容器。
- **kubectl**: 用于与 Kubernetes 集群进行交互。
- **Java 17+**: 用于在本地构建和运行后端服务。
- **Maven**: 用于构建 Java 项目。
- **Node.js 20+**: 用于构建和运行前端应用。
- **pnpm**: 用于管理 Node.js 依赖。

## 2. 本地开发环境部署

本地开发环境使用 Docker Compose 来管理基础设施（数据库、缓存、消息队列等），而应用服务则直接在本地运行，以便于调试和开发。

### 2.1 克隆项目

```bash
git clone https://github.com/your-username/ecommerce-microservices.git
cd ecommerce-microservices
```

### 2.2 启动基础设施

项目根目录下的 `docker/docker-compose.yml` 文件定义了所有需要的基础设施服务。你可以使用以下命令一键启动：

```bash
cd docker
docker-compose up -d
```

该命令会启动以下服务：

- PostgreSQL (数据库)
- Redis (缓存)
- RabbitMQ (消息队列)
- Nacos (服务注册与发现)
- MySQL (Nacos 依赖)

你可以通过 `docker-compose ps` 命令查看所有服务的运行状态。

### 2.3 启动后端服务

在你的 IDE (例如 IntelliJ IDEA) 中，打开 `backend` 目录下的 Maven 项目。然后，分别启动以下微服务：

- `user-service`
- `product-service`
- `order-service`
- `payment-service`
- `inventory-service`
- `gateway-service`

### 2.4 启动前端应用

```bash
cd frontend
pnpm install
pnpm dev
```

启动成功后，你可以在浏览器中访问 `http://localhost:5173` 来查看前端页面。

## 3. Docker 生产环境部署

在生产环境中，我们建议将所有服务都容器化，并使用 Docker Compose 进行编排。

### 3.1 构建 Docker 镜像

在构建镜像之前，你需要先构建项目产物：

```bash
# 构建后端 JAR 包
cd backend
mvn clean package -DskipTests

# 构建前端静态文件
cd ../frontend
pnpm install
pnpm run build
```

然后，在项目根目录下，执行以下命令构建 Docker 镜像：

```bash
# 构建后端服务镜像
docker build -t ecommerce/user-service:latest -f docker/Dockerfile.backend --build-arg SERVICE_NAME=user-service .
docker build -t ecommerce/product-service:latest -f docker/Dockerfile.backend --build-arg SERVICE_NAME=product-service .
# ... 为其他后端服务构建镜像

# 构建前端镜像
docker build -t ecommerce/frontend:latest -f docker/Dockerfile.frontend .
```

### 3.2 启动容器

```bash
cd docker
docker-compose -f docker-compose.prod.yml up -d
```

> **注意**: `docker-compose.prod.yml` 是一个示例文件，你需要根据实际情况修改其中的配置，例如镜像名称、端口映射、环境变量等。

## 4. Kubernetes 生产环境部署

对于大规模的生产环境，我们推荐使用 Kubernetes 进行容器编排和管理。

### 4.1 准备 Kubernetes 集群

你需要一个可用的 Kubernetes 集群。你可以使用云服务商提供的 Kubernetes 服务（例如 GKE, EKS, AKS），或者在自己的服务器上搭建一个 Kubernetes 集群。

### 4.2 推送 Docker 镜像

将构建好的 Docker 镜像推送到一个镜像仓库，例如 Docker Hub, Harbor, 或者云服务商提供的镜像仓库。

```bash
docker push ecommerce/user-service:latest
docker push ecommerce/product-service:latest
docker push ecommerce/frontend:latest
```

### 4.3 部署应用

项目中的 `kubernetes` 目录包含了所有部署所需的 YAML 文件。你可以使用我们提供的脚本一键部署：

```bash
cd scripts
./k8s-deploy.sh
```

该脚本会依次执行以下操作：

1.  创建 `ecommerce` 命名空间。
2.  创建 Secret 用于存储敏感信息。
3.  部署 PostgreSQL 和 Redis。
4.  部署所有微服务。
5.  部署前端应用。
6.  部署 Ingress 用于暴露服务。

部署完成后，你可以通过 `kubectl get pods -n ecommerce` 查看所有 Pod 的状态。

### 4.4 访问应用

部署完成后，你可以通过 Ingress 的地址来访问应用。你可以通过以下命令获取 Ingress 的地址：

```bash
kubectl get ingress -n ecommerce
```

## 5. CI/CD 自动化部署

本项目在 `.github/workflows` 目录下提供了一个简单的 GitHub Actions 工作流示例，用于实现 CI/CD 自动化部署。

### 5.1 工作流说明

-   **`build.yml`**: 当有代码推送到 `main` 或 `develop` 分支时，自动触发构建和测试。
-   **`deploy.yml`**: 当有新的 Tag 推送到 `main` 分支时，自动触发部署到生产环境。

### 5.2 配置 CI/CD

1.  在 GitHub 项目的 `Settings -> Secrets and variables -> Actions` 中，添加以下 Secret：
    *   `DOCKER_USERNAME`: Docker Hub 用户名
    *   `DOCKER_PASSWORD`: Docker Hub 密码
    *   `KUBE_CONFIG_DATA`: Kubernetes 集群的 kubeconfig 文件内容 (Base64 编码)

2.  修改 `deploy.yml` 文件中的镜像名称和部署脚本，以适应你的环境。

配置完成后，当你推送代码或创建新的 Tag 时，GitHub Actions 将会自动执行构建、测试和部署流程。
