# ZhuaTech Quiz Agent｜知华科技智能出题助手系统

## 本次新增：客观题加权评分与错题明细（2026-10-23）

按单选、多选答案集合及题目分值执行真实评分，区分未答、错答和正确，不用固定分数代替计算。

从工作台进入“专项业务工具”（`/practical.html`）。本次功能执行真实后端计算、支持错误提示、表格预览和完整结果导出；不把旧演示结果冒充成新功能实现。

轻量H5项目没有登录体系，本新增接口默认关闭（503）。部署时以环境变量ZHUATECH_PRACTICAL_TOKEN配置独立32–256位可打印ASCII令牌，然后在专项页面填写该令牌；令牌只保留在本页内存。Compose已透传该变量。不要把真实令牌写入前端、示例文件或Git。旧演示接口不因此变成生产授权体系。

接口：`POST /api/practical/weighted-exact-set-grading`。算法、样例、校验及测试命令见[专项升级说明](docs/AI_MULTIMODAL_UPGRADE_20261023.md)。不调用外部模型、不自动写业务单据。

[简体中文](README.md) | [English](README.en.md)

ZhuaTech Quiz Agent 是上海如静知华信息科技有限公司面向“培训题库管理”场景推出的社区源码项目。面向企业培训与知识考核的题目生成、审核和组卷系统。依据授权材料生成可追溯题目，并通过人工审核进入题库。

[知华科技官网](https://www.zhuatech.cn/) · Java 包名 `cn.zhuatech.quizagent` · API `POST /api/quizagent/run`

> 工程默认执行本地确定性演示逻辑，不内置模型、密钥和第三方数据。已预留 DeepSeek 兼容配置，使用者可自行接入并承担数据授权、输出复核和业务合规责任。

![知华科技智能出题助手工作台](docs/images/quizagent-dashboard.png)

界面围绕真实业务队列组织：上方展示核心运营指标，中间并列呈现处理队列与辅助分析，关键结论提供置信度、依据和人工确认入口，避免把模型输出直接写入正式业务。

## 一套可以继续扩展的业务骨架

| 模块 | 社区源码版能力 |
| --- | --- |
| 出题任务 | 覆盖 6 个主要知识点 |
| 题目审核 | 17 道题可定位到明确段落 |
| 企业题库 | 审核题干和标准答案 |
| 组卷管理 | 退回依据不足的 3 道题 |
| AI 扩展 | 本地演示管线、DeepSeek 兼容请求载荷、置信度阈值与人工复核状态 |
| 工程能力 | Java 21、Spring Boot 4、H5、MySQL 8、Docker Compose、JUnit 自动化测试 |

## 工程结构

`backend/` 提供可验证的 Java API，`frontend/` 是可直接运行的响应式 H5 工作台，`database/schema.sql` 给出 MySQL 业务表与审计表，`docs/images/` 保存实际页面截图。

## 本地运行

```bash
cd backend
mvn spring-boot:run
```

直接打开 `frontend/index.html` 即可使用前端演示；也可以运行完整容器：

```bash
docker compose up --build
```

访问 `http://localhost:8088`。即使后端未启动，页面仍会返回相同结构的本地演示结果。

## DeepSeek 接入预留

```dotenv
ZHUATECH_LLM_PROVIDER=local
ZHUATECH_LLM_BASE_URL=https://api.deepseek.com
ZHUATECH_LLM_MODEL=deepseek-chat
ZHUATECH_LLM_API_KEY=
```

建议在 `WorkspaceService` 外增加 Provider 接口，将外部调用放入独立适配器；API Key 只通过环境变量或密钥管理服务注入，不提交到源码仓库。生产环境还应补充组织权限、数据脱敏、调用审计、限流、失败重试和人工确认。

## 负责任使用

- 只处理已获授权的数据，不得上传无权访问的客户、员工或个人信息。
- AI 结果属于辅助信息，不能替代业务负责人、专业人员或法定审批人的决定。
- 高风险结论、对外承诺和正式业务写入必须经过人工复核。
- 本仓库不包含真实业务数据、生产账号、模型密钥或第三方受版权保护的素材。

## 许可、咨询与商业服务

本工程仅限个人学习、研究和非商业技术交流，**不得商用**。企业内部生产使用、SaaS 部署、软件实施、模型接入、品牌定制与项目交付，均须获得上海如静知华信息科技有限公司书面授权，详见 [LICENSE](LICENSE)。

| 微信咨询一 | 微信咨询二 |
| --- | --- |
| ![知华科技微信咨询二维码一](docs/images/zhuatech-wechat-consulting.png) | ![知华科技微信咨询二维码二](docs/images/zhuatech-wechat-consulting-2.png) |

深度开发、中小企业 AI 转型和软件项目外包请访问：[https://www.zhuatech.cn/](https://www.zhuatech.cn/)

SEO：智能出题助手,AI题库,自动组卷,培训考试,答案溯源,DeepSeek Java、企业 AI 转型、知华科技、上海软件外包、中小企业信息化。
