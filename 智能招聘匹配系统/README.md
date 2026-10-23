# 基于 Spring Boot 与 NLP 的智能招聘匹配系统

本项目是一个面向求职者、招聘者和管理员的招聘匹配系统。系统后端使用
Spring Boot，前端页面以静态资源方式集成在同一个项目中，提供岗位浏览、
简历管理、智能推荐、投递、收藏、评价、面试安排和数据管理等功能。

## 主要功能

- 游客：浏览首页、搜索岗位、查看岗位详情、查看热门岗位和排行榜。
- 求职者：注册登录、维护个人资料、编辑简历、查看智能推荐、收藏岗位、
  投递简历、查看投递记录、评价岗位和发表评论。
- 招聘者：发布岗位、查看收到的投递、更新投递状态、安排面试和查看招聘看板。
- 管理员：查看系统概览，管理用户、岗位、分类、热门岗位、用户组和投递状态。

## NLP 匹配说明

系统的智能匹配由 `project.service.NlpMatchService` 完成，运行在 Java 进程内，
不依赖外部 Python 服务、预训练模型或 GPU：

1. 使用技术词典和正则表达式从简历与岗位文本中提取关键词、手机号、
   邮箱和学历信息。
2. 将简历与岗位文本转换为词项向量。
3. 使用余弦相似度计算文本匹配程度。
4. 根据求职者学历和岗位学历要求应用学历修正系数，最终得到 0 至 100 的匹配分。

## 技术栈

- Java 17
- Spring Boot 3.3.5
- Spring Web、Spring Data JPA、Bean Validation
- H2 文件数据库（默认）
- MySQL 8.x（可选）
- HTML、CSS、JavaScript
- HMAC-SHA256 签名的轻量 JWT
- Apache POI、PDFBox（项目依赖）

## 项目结构

```text
智能招聘匹配系统/
├─ src/main/java/project/       后端 Java 源代码
├─ src/main/resources/
│  ├─ application.yml           数据库、端口和 JWT 配置
│  └─ static/                   HTML、CSS、JavaScript 页面
├─ data/                        H2 数据文件目录
├─ docs/
│  ├─ 环境配置与运行说明.md
│  └─ 数据库说明.md
├─ pom.xml                      Maven 项目配置
├─ README.md
├─ 作品文件清单.txt
└─ .gitignore
```

## 环境要求

- JDK 17
- Maven 3.9 或更高版本
- 使用默认配置时不需要单独安装数据库

检查环境：

```powershell
java -version
mvn -version
```

完整的安装、启动和故障排查步骤见
[环境配置与运行说明](docs/环境配置与运行说明.md)。

## 快速启动

在本项目目录中执行：

```powershell
mvn spring-boot:run
```

启动成功后访问：

```text
http://localhost:8080
```

默认使用 `dev` 配置和 H2 文件数据库，数据库文件位于
`data/recruitment.mv.db`。首次启动时，应用会自动建表并写入演示数据。
请从项目根目录启动，以保证 `./data` 路径正确。

## 演示账号

| 角色 | 账号 | 密码 |
| --- | --- | --- |
| 管理员 | `admin` | `123456` |
| 求职者 | `alice` | `123456` |
| 求职者 | `bob` | `123456` |
| 求职者 | `cindy` | `123456` |
| 招聘者 | `hr` | `123456` |

上述账号和密码仅用于课程作业演示，不应直接用于生产环境。

## 配置方式

默认配置见
[application.yml](src/main/resources/application.yml)。常用环境变量如下：

| 环境变量 | 默认值 | 用途 |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | 服务端口 |
| `SPRING_PROFILES_ACTIVE` | `dev` | 选择 `dev` 或 `mysql` |
| `APP_JWT_SECRET` | 内置演示值 | JWT 签名密钥 |
| `MYSQL_HOST` | `localhost` | MySQL 主机 |
| `MYSQL_PORT` | `3306` | MySQL 端口 |
| `MYSQL_DATABASE` | `smart_recruitment` | 数据库名 |
| `MYSQL_USERNAME` | `root` | MySQL 用户名 |
| `MYSQL_PASSWORD` | 空 | MySQL 密码 |

MySQL 的创建、切换和备份说明见
[数据库说明](docs/数据库说明.md)。

## 构建与运行

```powershell
mvn clean package
java -jar target/smart-recruitment-system-1.0.0.jar
```

打包后的 JAR 需要在包含 `pom.xml` 的项目根目录运行，或者显式配置 H2
数据库的绝对路径。端口被占用时，可以这样启动：

```powershell
$env:SERVER_PORT = "8081"
java -jar target/smart-recruitment-system-1.0.0.jar
```

## 数据重置

停止应用后，删除 `data/recruitment.mv.db`，再次启动时会重新建表并生成
演示数据。若需要保留已有用户、岗位和投递数据，请不要删除数据库文件。

## 提交说明

课程提交包应包含 `src`、`pom.xml`、配置文件和说明文档，不包含 `target`、
`.idea` 等构建或编辑器生成文件。GitHub 仓库应从本目录初始化，使其根目录
直接包含 `README.md`、`pom.xml` 和 `src`。
