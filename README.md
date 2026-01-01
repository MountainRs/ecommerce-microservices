> 本项目由 Manus AI 生成，旨在为计算机专业的学生提供一个符合业界主流实践的全栈微服务项目案例。通过学习和实践本项目，你将掌握从项目设计、开发、测试到部署的全流程，并深入理解微服务架构、容器化、自动化运维等核心技术。

# 电商微服务平台

这是一个基于 Java 和 Vue 的现代化电商微服务平台，旨在提供一个完整的、生产级别的项目实践方案。项目涵盖了从前端到后端的全栈技术，并采用了微服务架构、容器化部署、自动化运维等业界主流的最佳实践。

## ✨ 项目特色

- **微服务架构**: 基于 Spring Cloud Alibaba 构建，包含用户、商品、订单等多个微服务。
- **全栈技术**: 后端采用 Java + Spring Boot，前端采用 Vue 3 + TypeScript，数据库使用 PostgreSQL。
- **容器化部署**: 提供完整的 Docker 和 Kubernetes 配置，支持一键部署。
- **自动化运维**: 集成 CI/CD 流程，支持自动化构建、测试和部署。
- **生产级实践**: 包含日志、监控、告警、分布式事务等生产级特性。
- **完善的文档**: 提供详细的架构设计、API 设计、数据库设计、部署指南等中文文档。

## 🚀 快速开始

### 本地开发

1.  **环境准备**:
    *   安装 Docker 和 Docker Compose
    *   安装 JDK 17 和 Maven
    *   安装 Node.js 和 pnpm

2.  **启动基础设施**:

    ```bash
    cd docker
    docker-compose up -d
    ```

3.  **启动后端服务**:

    在 IDE 中分别启动各个微服务，例如 `user-service`、`product-service` 等。

4.  **启动前端应用**:

    ```bash
    cd frontend
    pnpm install
    pnpm dev
    ```

### Docker 部署

1.  **构建镜像**:

    ```bash
    # 构建后端镜像
    docker build -t ecommerce/user-service:latest -f docker/Dockerfile.backend --build-arg SERVICE_NAME=user-service .
    docker build -t ecommerce/product-service:latest -f docker/Dockerfile.backend --build-arg SERVICE_NAME=product-service .

    # 构建前端镜像
    docker build -t ecommerce/frontend:latest -f docker/Dockerfile.frontend .
    ```

2.  **启动容器**:

    ```bash
    cd docker
    docker-compose up -d
    ```

### Kubernetes 部署

1.  **环境准备**:
    *   安装 kubectl
    *   准备一个 Kubernetes 集群

2.  **部署应用**:

    ```bash
    cd scripts
    ./k8s-deploy.sh
    ```

## 📚 项目文档

- **[架构设计](docs/architecture.md)**: 了解项目的整体架构和技术选型。
- **[API 设计](docs/api-design.md)**: 查看各个微服务的 API 接口文档。
- **[数据库设计](docs/database-design.md)**: 了解数据库的表结构和设计思路。
- **[Git 工作流](docs/git-workflow.md)**: 学习本项目采用的 Git 分支模型和提交规范。
- **[部署指南](docs/deployment-guide.md)**: 获取详细的 Docker 和 Kubernetes 部署步骤。
- **[开发指南](docs/development-guide.md)**: 了解如何搭建本地开发环境和进行二次开发。

## 🛠️ 技术栈

| 分类 | 技术 | 版本 | 描述 |
| :--- | :--- | :--- | :--- |
| **后端** | Spring Boot | 3.1.x | 微服务基础框架 |
| | Spring Cloud Alibaba | 2022.x | 服务治理、配置中心 |
| | Spring Cloud Gateway | 4.0.x | API 网关 |
| | PostgreSQL | 14+ | 关系型数据库 |
| | Redis | 7.0+ | 分布式缓存 |
| | RabbitMQ | 3.12+ | 消息队列 |
| | Elasticsearch | 8.0+ | 搜索引擎 |
| **前端** | Vue | 3.x | 前端框架 |
| | TypeScript | 5.x | 类型安全 |
| | Vite | 5.x | 构建工具 |
| | Element Plus | 2.x | UI 组件库 |
| | Pinia | 2.x | 状态管理 |
| | Tailwind CSS | 3.x | 原子化 CSS |
| **DevOps** | Docker | 最新 | 应用容器化 |
| | Kubernetes | 1.27+ | 容器编排 |
| | Nginx | 最新 | 反向代理 |
| | Git / GitHub | | 版本控制 |
| | GitHub Actions | | CI/CD |

## 🤝 贡献

欢迎对本项目提出改进意见和贡献代码！你可以通过以下方式参与：

-   **提交 Issue**: 如果你发现了 Bug 或者有任何建议，欢迎提交 Issue。
-   **发起 Pull Request**: 如果你修复了 Bug 或者实现了新功能，欢迎发起 Pull Request。

在提交代码前，请确保你已经阅读并遵守了 [Git 工作流](docs/git-workflow.md) 中的规范。

## 📄 许可证

本项目采用 [MIT](LICENSE) 许可证。
