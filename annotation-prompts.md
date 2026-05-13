# Annotation Prompts

---

**1.** `[中等] [Bug 修复]` | 模块: `crmeb/crmeb-common/src/main/java/com/zbkj/common/validation/StringContainsValidator.java` | 技术栈: Java, Spring Validation | 功能模块: 配置

> `StringContainsValidator.isValid()` 方法中当 `value` 为空字符串时直接返回 `true`，这导致即使 `@StringContains` 注解的 `required` 属性设为 `true`，空白输入也能通过校验。请修复这个逻辑缺陷：当注解标记为必填且输入为空时，应返回 `false` 并设置自定义的 `constraintValidatorContext` 错误消息。

---

**2.** `[简单] [代码理解与分析]` | 模块: `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/impl/OrderTaskServiceImpl.java` | 技术栈: Java, SpringBoot, Redis | 功能模块: 操作日志

> 分析 `OrderTaskServiceImpl` 中的订单任务调度逻辑，输出分析文档到 `docs/analysis/order-task-flow.md`。文档需包含：1) 任务调度入口和执行流程 2) 各订单状态变更的触发条件 3) Redis 队列的使用方式和 key 设计 4) 事务边界和回滚策略。请阅读完整的 `OrderTaskServiceImpl` 和 `StoreOrderTaskServiceImpl` 后撰写。

---

**3.** `[简单] [功能迭代]` | 模块: `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/manager/CustomAccessDeniedHandler.java` | 技术栈: Java, Spring Security | 功能模块: 认证

> 当前 `CustomAccessDeniedHandler` 使用 `printStackTrace()` 处理 IO 异常，不符合生产环境的日志规范。请改用 Slf4j 日志框架记录错误，并在响应中返回更友好的 JSON 错误信息，包含：错误码、错误消息、请求路径（从 `httpServletRequest` 获取）。

---

**4.** `[中等] [功能迭代]` | 模块: `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/controller/StoreOrderController.java` | 技术栈: Java, SpringBoot, Mybatis Plus | 功能模块: API 调用

> 为订单管理控制器添加批量发货功能。当前 `/list` 接口只支持分页查询单个订单处理，需要新增 `POST /api/admin/store/order/batch-ship` 接口，接收订单 ID 列表和物流单号，支持批量更新订单状态为"已发货"。需要：1) 添加请求 DTO 2) 在 Controller 中新增端点 3) 在 Service 中实现批量处理逻辑，注意事务控制（部分失败不应回滚全部）。

---

**5.** `[简单] [Bug 修复]` | 模块: `crmeb/crmeb-common/src/main/java/com/zbkj/common/exception/CrmebException.java` | 技术栈: Java, SpringBoot | 功能模块: 操作日志

> `CrmebException` 的构造函数在拼接消息时使用 `iResultEnum.getCode() + "-" + iResultEnum.getMessage()`，但部分 `IResultEnum` 枚举的 `getMessage()` 可能返回 `null`，导致异常消息中出现 "null" 字面量。请添加空值保护，当 message 为 null 时使用 `iResultEnum.name()` 作为后备。

---

**6.** `[中等] [代码重构]` | 模块: `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/impl/StoreOrderServiceImpl.java` | 技术栈: Java, Mybatis Plus, Redis | 功能模块: 下载引擎

> `StoreOrderServiceImpl` 类过于臃肿，包含了订单创建、支付处理、退款、物流、统计等多类职责。请按职责拆分为 3 个独立的 Service 实现类：1) `OrderCreateServiceImpl` — 订单创建相关逻辑 2) `OrderPayServiceImpl` — 支付和退款逻辑 3) `OrderLogisticsServiceImpl` — 物流和发货逻辑。保持原有接口不变，使用委托模式转发调用到新的实现类。

---

**7.** `[困难] [功能迭代]` | 模块: `crmeb/crmeb-service/src/main/java/com/zbkj/service/service/impl/StoreBargainServiceImpl.java` | 技术栈: Java, Redis, Mybatis Plus | 功能模块: API 调用

> 为砍价活动添加防刷机制。当前实现中用户可通过脚本频繁发起砍价请求。请实现：1) 基于 Redis 的限流器，限制同一用户对同一砍价活动每分钟最多发起 5 次砍价 2) 添加 IP 维度的限制，同一 IP 每 10 秒最多发起 3 次砍价请求 3) 超过限制时返回友好的错误提示。修改范围：在 Controller 层或 Service 层加入拦截逻辑，新增限流配置常量。

---

**8.** `[简单] [测试]` | 模块: `crmeb/crmeb-common/src/test/java/` | 技术栈: Java, JUnit 4.12 | 功能模块: 单元测试

> 为 `CrmebException` 异常类编写完整的 JUnit 4 单元测试。测试文件输出到 `crmeb/crmeb-common/src/test/java/com/zbkj/common/exception/CrmebExceptionTest.java`。需覆盖：1) 各构造函数的参数传递正确性 2) `getCode()` 和 `getMessage()` 返回值 3) 带 Throwable 的构造函数中的异常链 4) 空参数构造函数的默认行为。

---

**9.** `[中等] [工程化]` | 模块: `crmeb/pom.xml` | 技术栈: Maven, Java | 功能模块: 工程配置

> 当前 Maven 多模块项目缺少统一的代码质量检查配置。请在根 `pom.xml` 中集成 Checkstyle 插件（使用 Google Java Style），并创建 `checkstyle.xml` 配置文件。要求：1) 在所有模块的 `validate` 阶段执行 Checkstyle 检查 2) 检查失败时构建失败 3) 在 `crmeb-common` 模块中先修复 5 个 checkstyle 违规项作为示例。

---

**10.** `[简单] [代码理解与分析]` | 模块: `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/service/impl/AdminLoginServiceImpl.java` | 技术栈: Java, Spring Security, JWT | 功能模块: 认证

> 分析管理端登录认证流程，输出分析文档到 `docs/analysis/admin-auth-flow.md`。文档需包含：1) 登录请求处理链路（Controller → Service → Token 生成）2) Spring Security 配置的角色和权限验证模型 3) Token 的生成、存储和验证机制 4) 密码加密方式。请阅读 `AdminLoginController`、`AdminLoginServiceImpl` 和相关的 Security 配置类后撰写。

---

**11.** `[中等] [功能迭代]` | 模块: `crmeb/crmeb-front/src/main/java/com/zbkj/front/controller/` | 技术栈: Java, SpringBoot, REST | 功能模块: API 调用

> 前端商城缺少"浏览历史"功能。请在 `crmeb-front` 模块中实现用户商品浏览记录功能：1) 在 `UserCenterController` 中新增 `/api/front/user/browse-history` GET 接口，分页返回用户最近浏览的商品 2) 在 `ProductController` 的商品详情接口中自动记录浏览行为到 Redis（商品 ID + 浏览时间戳，按用户维度存储，最多保留 50 条）3) 需要新增请求/响应 DTO。

---

**12.** `[简单] [调试]` | 模块: `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/task/order/` | 技术栈: Java, Spring Task, Redis | 功能模块: 操作日志

> 订单相关的定时任务（`OrderCancelTask`、`OrderCompleteTask`、`OrderReceiptTask`）在运行时缺少关键日志，导致生产环境问题难以排查。请为每个 Task 的 `execute` 方法添加结构化日志：1) 任务开始执行时记录任务名称和当前时间 2) 处理每个订单时记录订单 ID 和当前状态 3) 任务执行完成后记录处理总数、成功数、失败数 4) 异常时记录完整堆栈和失败的订单 ID。使用 Slf4j 的占位符格式。

---

**13.** `[中等] [Bug 修复]` | 模块: `crmeb/crmeb-admin/src/main/java/com/zbkj/admin/filter/ResponseWrapper.java` | 技术栈: Java, Servlet, SpringBoot | 功能模块: 编解码

> `ResponseWrapper` 用于捕获响应内容，但如果响应体超过缓冲区大小（默认 2KB），会导致内容截断。请修复：1) 使用动态扩展的 `ByteArrayOutputStream` 替代固定大小缓冲区 2) 添加响应体大小限制（默认 10MB，可通过配置调整），超过限制时记录警告日志 3) 确保字符编码始终使用 UTF-8。

---

**14.** `[困难] [代码重构]` | 模块: `crmeb/crmeb-common/src/main/java/com/zbkj/common/result/` | 技术栈: Java, 枚举模式 | 功能模块: 配置

> 项目中使用了大量结果码枚举（`AdminResultCode`、`OrderResultCode`、`ProductResultCode` 等 15+ 个），分散在各处且存在重复的 code 值。请重构：1) 创建统一的 `ResultCodeEnum` 枚举，将所有分散的结果码合并 2) 按模块划分 code 范围（管理员 1000-1999，订单 2000-2999，商品 3000-3999 等）3) 更新所有引用旧枚举的代码 4) 保留旧枚举作为 `@Deprecated` 的代理，指向新枚举，确保向后兼容。

---

**15.** `[简单] [代码理解与分析]` | 模块: `crmeb/crmeb-common/src/main/java/com/zbkj/common/utils/CrmebDateUtil.java` | 技术栈: Java, Hutool, Date API | 功能模块: 元数据处理

> 分析项目中使用的日期工具类 `CrmebDateUtil`，输出分析文档到 `docs/analysis/date-util-analysis.md`。文档需包含：1) 所有公共方法的签名和用途列表 2) 哪些方法是已有 Hutool `DateUtil` 的简单封装（可以直接替换）3) 哪些方法包含项目特有的业务逻辑（如营销活动时间计算）4) 建议：哪些方法可以废弃并用标准库或 Hutool 替代。请同时检查 `CrmebDateUtil` 在项目中的全部调用点。

---
