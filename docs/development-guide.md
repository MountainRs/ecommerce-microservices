# 开发指南

本指南旨在帮助开发者快速搭建本地开发环境，并顺利地参与到电商微服务平台的开发中。我们推荐使用 IntelliJ IDEA 作为后端开发 IDE，使用 Visual Studio Code 作为前端开发 IDE。

## 1. 环境准备

在开始之前，请确保你的开发环境中已经安装了以下软件：

- **Git**: 用于代码版本控制。
- **Docker & Docker Compose**: 用于运行本地的基础设施服务。
- **JDK 17+**: Java 开发工具包。
- **Maven**: Java 项目构建工具。
- **Node.js 20+**: 前端开发环境。
- **pnpm**: 高效的 Node.js 包管理器。

## 2. 克隆项目

首先，从 GitHub 克隆项目到你的本地：

```bash
git clone https://github.com/your-username/ecommerce-microservices.git
cd ecommerce-microservices
```

## 3. 启动基础设施

本项目依赖于 PostgreSQL, Redis, RabbitMQ, Nacos 等基础设施服务。我们已经通过 Docker Compose 将这些服务进行了编排，你可以使用以下命令一键启动：

```bash
cd docker
docker-compose up -d
```

启动后，你可以通过以下地址访问各个服务的管理界面：

- **RabbitMQ**: `http://localhost:15672` (用户名/密码: guest/guest)
- **Nacos**: `http://localhost:8848/nacos` (用户名/密码: nacos/nacos)

## 4. 后端开发

### 4.1 导入项目

使用 IntelliJ IDEA 打开项目根目录下的 `backend` 目录，IDEA 会自动识别并导入 Maven 项目。

### 4.2 配置 IDE

- **Lombok**: 确保你的 IDE 已经安装了 Lombok 插件，并开启了 "Enable annotation processing"。
- **Java 版本**: 确保项目的 SDK 设置为 Java 17。

### 4.3 运行服务

在 IDE 中，你可以分别运行每个微服务的启动类，例如 `UserServiceApplication`。为了方便，我们建议你配置好多模块的启动项，以便一键启动所有服务。

### 4.4 调试服务

你可以直接在 IDE 中对代码进行断点调试。由于服务都注册到了 Nacos，服务间的调用会自动进行负载均衡。

## 5. 前端开发

### 5.1 导入项目

使用 Visual Studio Code 打开项目根目录下的 `frontend` 目录。

### 5.2 安装依赖

我们推荐使用 `pnpm` 来管理前端依赖。在 `frontend` 目录下执行以下命令安装依赖：

```bash
pnpm install
```

### 5.3 运行应用

```bash
pnpm dev
```

该命令会启动一个本地开发服务器，你可以在浏览器中访问 `http://localhost:5173` 来查看前端页面。Vite 提供了热更新功能，当你修改代码时，页面会自动刷新。

### 5.4 API 代理

在 `vite.config.ts` 文件中，我们已经配置了 API 代理，将所有 `/api` 开头的请求代理到后端的 API 网关 (`http://localhost:8080`)。这样可以解决开发过程中的跨域问题。

## 6. 代码提交

在进行代码提交时，请务必遵循我们在 [Git 工作流指南](git-workflow.md) 中定义的规范。

- **分支命名**: `feature/your-feature-name`
- **提交信息**: `feat(module): add some feature`

## 7. 常见问题

- **Nacos 启动失败**: 请检查 `docker-compose.yml` 中 Nacos 的配置是否正确，以及 MySQL 是否正常启动。
- **服务注册失败**: 请检查微服务的 `application.yml` 文件中 Nacos 的地址是否配置为 `nacos:8848`。
- **前端 API 请求失败**: 请检查后端的 API 网关是否正常启动，以及前端的代理配置是否正确。

希望本指南能帮助你快速上手项目开发。如果你在开发过程中遇到任何问题，欢迎随时在项目的 Issue 中提出。
