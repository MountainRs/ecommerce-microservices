# API 设计规范

本规范旨在为电商微服务平台的 API 设计提供统一的标准，以确保 API 的一致性、可读性和易用性。所有微服务的 API 都应遵循本规范。

## 1. RESTful API 设计原则

我们采用 RESTful 风格来设计 API，遵循以下核心原则：

- **资源 (Resource)**: API 的核心是资源。每个资源都应该有一个唯一的 URI (统一资源标识符)。
- **HTTP 方法 (Verb)**: 使用标准的 HTTP 方法来表示对资源的操作。
- **状态码 (Status Code)**: 使用标准的 HTTP 状态码来表示请求的结果。
- **无状态 (Stateless)**: 每个请求都应该包含所有必要的信息，服务端不应该保存客户端的状态。
- **统一接口 (Uniform Interface)**: API 的设计应该保持一致，遵循统一的命名规范和数据格式。

## 2. URI 命名规范

- **使用名词复数**: URI 应该使用名词复数来表示资源集合，例如 `/users`, `/products`, `/orders`。
- **使用小写字母**: URI 应该全部使用小写字母，并使用连字符 `-` 来分隔单词，例如 `/user-profiles`。
- **避免使用动词**: URI 应该表示资源，而不是操作。操作应该通过 HTTP 方法来表示。
- **版本控制**: API 的版本应该通过 URI 来控制，例如 `/api/v1/users`。

## 3. HTTP 方法

| 方法 | 描述 | 示例 |
| :--- | :--- | :--- |
| `GET` | 获取资源 | `GET /api/v1/users` (获取所有用户)<br>`GET /api/v1/users/123` (获取 ID 为 123 的用户) |
| `POST` | 创建资源 | `POST /api/v1/users` (创建一个新用户) |
| `PUT` | 更新整个资源 | `PUT /api/v1/users/123` (更新 ID 为 123 的用户) |
| `PATCH` | 更新部分资源 | `PATCH /api/v1/users/123` (更新 ID 为 123 的用户的部分信息) |
| `DELETE` | 删除资源 | `DELETE /api/v1/users/123` (删除 ID 为 123 的用户) |

## 4. 数据格式

- **JSON**: 所有请求和响应的数据都应该使用 JSON 格式。
- **命名规范**: JSON 的字段名应该使用驼峰命名法 (camelCase)，例如 `userName`, `orderId`。
- **统一响应格式**: 所有 API 的响应都应该遵循统一的格式，包含响应码、消息和数据。

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 123,
    "username": "testuser"
  }
}
```

## 5. HTTP 状态码

| 状态码 | 描述 |
| :--- | :--- |
| `200 OK` | 请求成功 |
| `201 Created` | 资源创建成功 |
| `204 No Content` | 请求成功，但没有返回内容 |
| `400 Bad Request` | 请求参数错误 |
| `401 Unauthorized` | 未授权 |
| `403 Forbidden` | 禁止访问 |
| `404 Not Found` | 资源不存在 |
| `500 Internal Server Error` | 服务器内部错误 |

## 6. 认证与授权

- **JWT**: 使用 JSON Web Token (JWT) 进行用户认证。
- **Authorization Header**: 每个需要认证的请求都应该在 `Authorization` Header 中携带 JWT Token，格式为 `Bearer <token>`。
- **RBAC**: 基于角色的访问控制 (Role-Based Access Control)，通过用户的角色来控制对资源的访问权限。

## 7. API 文档

- **Swagger**: 使用 Swagger (OpenAPI) 来生成和维护 API 文档。
- **在线文档**: 每个微服务都应该提供一个在线的 API 文档，方便开发者查阅和测试。

## 8. 用户服务 API

### 8.1 用户注册

- **URL**: `POST /api/v1/users/register`
- **请求体**:

  ```json
  {
    "username": "testuser",
    "email": "testuser@example.com",
    "password": "password123",
    "confirmPassword": "password123"
  }
  ```

- **响应**:

  ```json
  {
    "code": 200,
    "message": "注册成功",
    "data": {
      "id": 1,
      "username": "testuser",
      "email": "testuser@example.com"
    }
  }
  ```

### 8.2 用户登录

- **URL**: `POST /api/v1/users/login`
- **请求体**:

  ```json
  {
    "username": "testuser",
    "password": "password123"
  }
  ```

- **响应**:

  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "userId": 1,
      "username": "testuser",
      "token": "<jwt-token>",
      "tokenType": "Bearer",
      "expiresIn": 86400000
    }
  }
  ```

## 9. 商品服务 API

### 9.1 获取商品列表

- **URL**: `GET /api/v1/products`
- **查询参数**:
  - `page` (可选): 页码，默认为 0
  - `size` (可选): 每页数量，默认为 10
  - `categoryId` (可选): 分类 ID
  - `keyword` (可选): 搜索关键词
- **响应**:

  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "content": [
        {
          "id": 1,
          "name": "商品 A",
          "price": 99.99
        }
      ],
      "totalElements": 1,
      "totalPages": 1
    }
  }
  ```

### 9.2 获取商品详情

- **URL**: `GET /api/v1/products/{productId}`
- **响应**:

  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "name": "商品 A",
      "description": "这是一个商品",
      "price": 99.99,
      "stockQuantity": 100
    }
  }
  ```

