# Servlet Book Library 基础项目

## 项目简介
这是一个基于 Java Servlet + JSP 的简易图书管理系统，包含管理员与读者两套登录入口，覆盖图书、分类、读者、借阅记录的常见增删改查与借阅流程。核心业务通过注解式 Servlet 暴露，例如 `/login` 负责管理员/读者登录，`/book` 负责图书的列表、编辑与删除，以及封面上传校验。项目默认将页面放在 `/WEB-INF/pages` 下，通过 `/page?act=xxx` 进行转发以避免直接暴露 JSP。【F:src/com/test/servlet/LoginServlet.java†L18-L64】【F:src/com/test/servlet/BookServlet.java†L25-L151】【F:src/com/test/servlet/PageServlet.java†L12-L28】【F:src/com/test/util/SystemConstants.java†L11-L25】

## 功能特性
- **账户体系**：区分管理员与读者，使用 `type` 参数选择登录身份；登录成功后将用户信息存入 Session 以控制后续访问权限。【F:src/com/test/servlet/LoginServlet.java†L28-L63】【F:src/com/test/util/SystemConstants.java†L11-L21】
- **图书管理**：支持分页查询、创建/编辑、删除以及封面文件上传与大小、类型校验，覆盖后台管理与借阅展示所需的数据接口。【F:src/com/test/servlet/BookServlet.java†L42-L200】
- **页面导航**：统一入口 `/page` 根据 `act` 参数转发到对应 JSP，默认进入登录页，便于集中管理页面路由。【F:src/com/test/servlet/PageServlet.java†L12-L28】
- **状态枚举**：统一的用户、图书、借阅状态常量便于前后端一致化渲染与校验。【F:src/com/test/util/SystemConstants.java†L15-L29】

## 目录结构
- `src/`：业务代码，包含 Servlet（控制层）、DAO（数据访问）、POJO（实体）、Filter（过滤器）与通用工具类。
- `web/`：Web 资源根目录，`WEB-INF/pages` 存放 JSP 视图，`static/` 提供 Bootstrap、jQuery 等前端依赖及上传目录，`WEB-INF/lib` 预置所需第三方 JAR。
- `testservlet.sql`：MySQL 初始化脚本，包含表结构及示例数据（管理员账号等）。【F:testservlet.sql†L20-L80】

## 环境要求
- JDK 8 及以上
- 支持 Servlet 4.0 的容器（如 Apache Tomcat 9）
- MySQL 8.x

## 数据库准备
1. 在 MySQL 中创建名为 `testservlet` 的数据库，执行项目根目录的 `testservlet.sql` 导入表结构与示例数据，默认包含 `admin/111111` 与 `test/111111` 等管理员账号。【F:testservlet.sql†L20-L37】
2. 根据实际环境修改 `src/com/test/util/DbUtils.java` 中的数据库连接 URL、用户名与密码后重新编译部署。【F:src/com/test/util/DbUtils.java†L10-L34】

## 运行与部署
1. 将 `web` 目录作为 Web 应用根目录（Web Resource Directory），`src` 作为源码根目录，在 IDE 中创建 Web Application 项目或打包为 WAR。
2. 确保 `web/WEB-INF/lib` 下的依赖被包含到运行时类路径。
3. 部署到本地 Tomcat 后，访问 `http://localhost:8080/page?act=login` 进行登录；登录成功会跳转到首页 `/page?act=index`。

## 关键模块速览
- **登录与退出**：`LoginServlet` 处理身份校验，`LogoutServlet`（`/logout`）清理 Session 并重定向到登录页。【F:src/com/test/servlet/LoginServlet.java†L28-L64】【F:src/com/test/servlet/LogoutServlet.java†L1-L45】
- **图书/分类/读者管理**：`BookServlet`、`CategoryServlet`、`ReaderServlet` 提供分页查询与编辑入口，表单数据通过 `RequestUtil` 做基础转换。【F:src/com/test/servlet/BookServlet.java†L42-L200】【F:src/com/test/servlet/CategoryServlet.java†L1-L132】【F:src/com/test/servlet/ReaderServlet.java†L1-L167】
- **借阅流程**：`BorrowServlet` 面向读者提供可借图书列表与借阅操作，结合库存与账号状态校验；`RecordServlet` 负责管理员查看与更新借阅记录状态。【F:src/com/test/servlet/BorrowServlet.java†L19-L79】【F:src/com/test/servlet/RecordServlet.java†L1-L141】
- **统一常量与工具**：`SystemConstants` 定义会话 Key、页面路径及业务枚举；`DbUtils` 封装数据库连接；`RequestUtil` 提供参数解析助手。【F:src/com/test/util/SystemConstants.java†L11-L29】【F:src/com/test/util/DbUtils.java†L10-L88】【F:src/com/test/util/RequestUtil.java†L1-L119】

