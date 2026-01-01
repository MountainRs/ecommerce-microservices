# Git 工作流指南

本项目采用 **Git Flow** 作为核心分支模型，并结合 **Conventional Commits** 规范来管理代码的提交和版本发布。本指南旨在帮助所有参与者理解并遵循统一的 Git 工作流，以确保代码的质量和项目的顺利进行。

## 1. 分支模型：Git Flow

Git Flow 是一种成熟的分支管理策略，它通过定义不同类型的分支来隔离不同阶段的开发工作，从而使多人协作更加清晰和高效。在本项目中，我们主要使用以下五种分支：

-   `main`: **主分支**，用于存放生产环境的稳定代码。该分支的代码应该是随时可以部署的。
-   `develop`: **开发分支**，用于集成各个功能分支。所有新的功能开发都应该从 `develop` 分支开始。
-   `feature/*`: **功能分支**，用于开发新的功能。每个功能都应该在一个独立的功能分支中进行开发。
-   `release/*`: **发布分支**，用于准备新的版本发布。在该分支上，我们主要进行版本号的更新、文档的生成以及最后的测试。
-   `hotfix/*`: **热修复分支**，用于修复生产环境的紧急 Bug。当生产环境出现问题时，我们应该从 `main` 分支创建一个热修复分支，修复问题后合并回 `main` 和 `develop` 分支。

### 1.1 分支命名规范

-   **功能分支**: `feature/<feature-name>`，例如 `feature/user-authentication`
-   **发布分支**: `release/<version-number>`，例如 `release/v1.0.0`
-   **热修复分支**: `hotfix/<issue-name>`，例如 `hotfix/login-bug`

### 1.2 工作流程示例

1.  **开始新功能开发**:

    ```bash
    # 切换到 develop 分支并拉取最新代码
    git checkout develop
    git pull origin develop

    # 创建新的功能分支
    git checkout -b feature/new-feature
    ```

2.  **完成功能开发**:

    ```bash
    # 提交代码
    git add .
    git commit -m "feat: add new feature"

    # 推送到远程仓库
    git push origin feature/new-feature
    ```

3.  **发起 Pull Request**:

    在 GitHub 上，从 `feature/new-feature` 分支向 `develop` 分支发起一个 Pull Request。在 Pull Request 中，详细描述你所做的修改，并@相关人员进行代码审查。

4.  **代码审查与合并**:

    在代码审查通过后，将 Pull Request 合并到 `develop` 分支，并删除功能分支。

## 2. 提交规范：Conventional Commits

为了使提交历史更加清晰和易于追溯，我们采用 **Conventional Commits** 规范来编写提交信息。该规范要求提交信息遵循以下格式：

```
<type>(<scope>): <subject>

[optional body]

[optional footer]
```

### 2.1 提交类型 (Type)

| 类型 | 描述 |
| :--- | :--- |
| `feat` | 新功能 (feature) |
| `fix` | 修复 Bug |
| `docs` | 文档变更 |
| `style` | 代码格式（不影响代码运行的变动） |
| `refactor` | 重构（既不是新增功能，也不是修改 Bug 的代码变动） |
| `perf` | 性能优化 |
| `test` | 增加测试 |
| `chore` | 构建过程或辅助工具的变动 |
| `revert` | 回滚到上一个版本 |

### 2.2 范围 (Scope)

范围用于说明本次提交影响的模块或组件，例如 `user-service`、`frontend`、`docs` 等。如果本次提交影响了多个模块，可以使用 `*` 或者省略范围。

### 2.3 主题 (Subject)

主题是本次提交的简短描述，应该清晰地说明本次提交的目的。主题应该以动词开头，采用现在时态，例如 `add`、`change`、`fix` 等。

### 2.4 示例

-   **新增功能**:

    ```
    feat(user-service): add user registration endpoint
    ```

-   **修复 Bug**:

    ```
    fix(frontend): correct login form validation
    ```

-   **文档变更**:

    ```
    docs: update README with new setup instructions
    ```

## 3. Pull Request (PR) 规范

-   **标题**: PR 的标题应该清晰地描述本次变更的内容，格式与 Conventional Commits 的主题类似。
-   **描述**: PR 的描述应该详细说明本次变更的背景、目的和实现方式。如果关联了 Issue，应该在描述中添加 `Closes #<issue-number>`。
-   **代码审查**: 在发起 PR 后，应该@至少一位相关人员进行代码审查。在审查通过后，才能合并到目标分支。

## 4. 版本发布

当 `develop` 分支集成了足够多的功能后，我们就可以准备发布一个新的版本。版本发布流程如下：

1.  **创建发布分支**:

    ```bash
    git checkout -b release/v1.1.0 develop
    ```

2.  **更新版本号**:

    在发布分支上，更新 `pom.xml`、`package.json` 等文件中的版本号。

3.  **生成变更日志**:

    根据 Conventional Commits 的提交历史，自动生成本次发布的变更日志。

4.  **合并到主分支和开发分支**:

    ```bash
    # 合并到 main 分支
    git checkout main
    git merge --no-ff release/v1.1.0
    git tag -a v1.1.0 -m "Release v1.1.0"

    # 合并回 develop 分支
    git checkout develop
    git merge --no-ff release/v1.1.0
    ```

5.  **推送到远程仓库**:

    ```bash
    git push origin main
    git push origin develop
    git push origin --tags
    ```

通过遵循以上 Git 工作流，我们可以更好地管理项目的代码，提高开发效率，并确保代码的质量和项目的稳定、高质量的版本发布。
