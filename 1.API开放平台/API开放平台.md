# API开放平台

## 项目介绍

一个提供 API 接口供开发者调用的平台。
用户可以注册登录，开通接口调用权限。用户可以浏览接口并调用，且每次调用会进行统计。
管理员可以发布接口、下线接口、接入接口，以及可视化接口的调用情况、数据。
项目侧重于后端，包含较多的编程技巧和架构设计层面的知识。

## 第一期

主要内容：

- 项目介绍、业务流程、项目计划、需求分析
- 数据库表设计
- 前后端项目初始化（包含 Ant Design Pro 框架最新版本的使用）
- 前后端代码自动生成（强烈推荐，提高 1000% 的开发效率）
- 登录页、接口信息页开发

### 需求分析

背景：

- 前端开发需要用到后台接口
- 使用现成的系统的功能（http://api.btstu.cn/）

做一个 API 接口平台：

- 管理员可以对接口信息进行增删改查
- 用户可以访问前台，查看接口信息

其他要求：

- 防止攻击（安全性）
- 不能随便调用（限制、开通）
- 统计调用次数
- 计费
- 流量保护
- API 接入

### 业务流程

![image-20230123224102839](D:\Typora\images\image-20230123224102839.png)

### 技术选型

**前端**

- Ant Design Pro 
- React
- Ant Design Procomponents
- Umi
- Umi Request（Axios 的封装）

**后端**

- Java Spring Boot
- Spring Boot Starter（SDK 开发）
- Dubbo（RPC）
- Nacos
- Spring Cloud Gateway（网关、限流、日志实现）

### 数据库表设计

使用https://www.sqlfather.com/生成数据库表；

![image-20230123224355827](D:\Typora\images\image-20230123224355827.png)

### 项目脚手架

前端：ant design pro 脚手架（https://pro.ant.design/zh-CN/）

直接下载星球后端代码模板（见星球代码库：https://t.zsxq.com/08DElcxT7 => springboot-init 项目）

### 基础功能开发

增删改查、登录功能（通过复制粘贴完成）

前端接口调用：后端使用遵循 openapi 的规范的 swagger 文档，使用前端 Ant Design Pro 框架集成的 oneapi 插件自动生成。

## 第二期

主要内容：

- 开发接口管理前端页面
- 开发模拟 API 接口
- 开发调用接口客户端
- 保证调用的安全性（API 签名认证）
- 客户端 SDK 的开发（Spring Boot Starter 开发）

### 模拟接口项目

项目名称：proapi-interface

提供三个不同种类的模拟接口：

- GET 接口
- POST 接口（url 传参）
- POST 接口（Restful）

**调用接口**

几种 HTTP 调用方式：

- HttpClient
- RestTemplate
- 第三方库（OKHTTP、Hutool）

Hutool：https://hutool.cn/docs/#/
Http 工具类：[https://hutool.cn/docs/#/http/Http%E5%AE%A2%E6%88%B7%E7%AB%AF%E5%B7%A5%E5%85%B7%E7%B1%BB-HttpUtil](https://hutool.cn/docs/#/http/Http客户端工具类-HttpUtil)

### API 签名认证

本质：

- 签发签名
- 使用签名（校验签名）

为什么需要？

- 保证安全性，不能随便一个人调用
- 适用于无需保存登录态的场景。只认签名，不关注用户登录态。

**签名认证实现**

通过 http request header 头传递参数。

- 参数 1：accessKey：调用的标识 userA, userB（复杂、无序、无规律）

- 参数 2：secretKey：密钥（复杂、无序、无规律）**该参数不能放到请求头中**（类似用户名和密码，区别：ak、sk 是无状态的）

==大家可以自己写代码来给用户生成 ak、sk==

千万不能把密钥直接在服务器之间传递，有可能会被拦截；

- 参数 3：用户请求参数
- 参数 4：签名

加密方式：对称加密、非对称加密、md5 签名（不可解密）

用户参数 + 密钥 => **签名生成算法（MD5、HMac、Sha1）** => 不可解密的值
abc + abcdefgh => sajdgdioajdgioa

**怎么知道这个签名对不对？**

服务端用一模一样的参数和算法去生成签名，只要和用户传的的一致，就表示一致。

**怎么防重放？**

- 参数 5：加 nonce 随机数，只能用一次，服务端要保存用过的随机数；

- 参数 6：加 timestamp 时间戳，校验时间戳是否过期。

**API 签名认证是一个很灵活的设计，具体要有哪些参数、参数名如何一定要根据场景来。（比如 userId、appId、version、固定值等）**

思考：难道开发者每次调用接口都要自己写签名算法？

### 开发简单易用的 SDK（简历加分项）

项目名：proapi-client-sdk

**为什么需要 Starter？**

理想情况：开发者只需要关心调用哪些接口、传递哪些参数，就跟调用自己写的代码一样简单。
开发 starter 的好处：开发者引入之后，可以直接在 application.yml 中写配置，自动创建客户端；

**Starter 开发流程**

初始化，环境依赖（一定要移除 build）：

spring-boot-configuration-processor 的作用是自动生成配置的代码提示

![image-20230123225709550](D:\Typora\images\image-20230123225709550.png)

编写配置类（启动类）：

![image-20230123225731216](D:\Typora\images\image-20230123225731216.png)

注册配置类，resources/META_INF/spring.factories 文件：

![image-20230123225749450](D:\Typora\images\image-20230123225749450.png)

mvn install 打包代码为本地依赖包

创建新项目（复用 server 项目）、测试

## 第三期

主要内容：

- 开发接口发布 / 下线的功能
- 开发浏览接口、查看接口文档、申请签名功能
- 开发在线调试功能

### 接口发布 / 下线的功能

权限控制：仅管理员可操作

**业务逻辑**

发布接口：

- 校验该接口是否存在
- 判断该接口是否可以调用
- 修改接口数据库中的状态字段为 1

下线接口：

- 校验该接口是否存在
- 修改接口数据库中的状态字段为 0

### 查看接口文档

思路：动态路由，用 url 来传递 id，加载不同的接口信息

### 申请签名

用户在注册成功时，自动分配 accessKey、secretKey

### 在线调用

请求参数的类型（直接用 json 类型，更灵活）：

![image-20230123230647179](D:\Typora\images\image-20230123230647179.png)

先跑通整个接口流程，后续可以针对不同的请求头或者接口类型来设计界面和表单，给用户更好的体验。（可以参考 swagger、postman、knife4j）

**调用流程**

使用上述流程：

![image-20230123230712509](D:\Typora\images\image-20230123230712509.png)

- 前端将用户输入的请求参数和要测试的接口 id 发给平台后端
- （在调用前可以做一些校验）
- 平台后端去调用模拟接口

### todo

- 判断该接口是否可以调用时由固定方法名改为根据测试地址来调用
- 用户测试接口固定方法名改为根据测试地址来调用
- 模拟接口改为从数据库校验 akey

## 第四期

主要内容：

- 开发接口调用次数统计功能
- 优化整个系统的架构 - API 网关详解 
  - 网关是什么？
  - 网关的作用？
  - 网关的应用场景及实现？
  - 结合业务去应用网关

### 接口调用次数统计

需求：

- 用户每次调用接口成功，次数 + 1
- 给用户分配或者用户自主申请接口调用次数

业务流程：

- 用户调用接口（之前已完成）
- 修改数据库，调用次数 +1

设计库表：

- 哪个用户？哪个接口？
- 用户 => 接口（多对多）

用户调用接口关系表：

![image-20230123230904776](D:\Typora\images\image-20230123230904776.png)

开发步骤：

- 开发基本增删改查（给管理员用）
- 开发用户调用接口次数 +1 的功能（service）

**问题**

- 如果每个接口的方法都写调用次数 + 1，是不是比较麻烦？
- 致命问题：接口开发者需要自己去添加统计代码

![image-20230123234338464](D:\Typora\images\image-20230123234338464.png)

使用 AOP 切面的优点：独立于接口，在每个接口调用后统计次数 + 1

AOP 切面的缺点：只存在于单个项目中，如果每个团队都要开发自己的模拟接口，那么都要写一个切面

### 网关

什么是网关？理解成火车站的检票口，统一 去检票。

**作用**

统一去进行一些操作、处理一些问题。
比如：

- 路由

  起到转发的作用，比如有接口 A 和接口 B，网关会记录这些信息，根据用户访问的地址和参数，转发请求到对应的接口（服务器 / 集群）
  /a => 接口A
  /b => 接口B
  https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#gateway-request-predicates-factories

- 负载均衡

  在路由的基础上
  /c => 服务 A / 集群 A（随机转发到其中的某一个机器）
  uri 从固定地址改成 lb:xxxx

- 统一鉴权

  判断用户是否有权限进行操作，无论访问什么接口，我都统一去判断权限，不用重复写。

- 跨域

  网关统一处理跨域，不用在每个项目里单独处理
  https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#cors-configuration

- 统一业务处理（缓存）

  把一些每个项目中都要做的通用逻辑放到上层（网关），统一处理，比如本项目的次数统计

- 访问控制

  黑白名单，比如限制 DDOS IP

- 发布控制

  灰度发布，比如上线新接口，先给新接口分配 20% 的流量，老接口 80%，再慢慢调整比重。
  https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-weight-route-predicate-factory

- 流量染色

  给请求（流量）添加一些标识，一般是设置请求头中，添加新的请求头
  https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-addrequestheader-gatewayfilter-factory
  全局染色：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#default-filters

- 接口保护

  - 限制请求

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#requestheadersize-gatewayfilter-factory

  - 信息脱敏

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-removerequestheader-gatewayfilter-factory

  - 降级（熔断）

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#fallback-headers

  - 限流：学习令牌桶算法、学习漏桶算法，学习一下 RedisLimitHandler

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-requestratelimiter-gatewayfilter-factory

  - 超时时间

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#http-timeouts-configuration

  - 重试（业务保护）

    https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-retry-gatewayfilter-factory

- 统一日志

  统一的请求、响应信息记录

- 统一文档

  将下游项目的文档进行聚合，在一个页面统一查看
  建议用：https://doc.xiaominfo.com/docs/middleware-sources/aggregation-introduction

### 网关的分类

全局网关（接入层网关）：作用是负载均衡、请求日志等，不和业务逻辑绑定

业务网关（微服务网关）：会有一些业务逻辑，作用是将请求转发到不同的业务 / 项目 / 接口 / 服务

参考文章：https://blog.csdn.net/qq_21040559/article/details/122961395

### 网关实现

Nginx（全局网关）、Kong 网关（API 网关，Kong：https://github.com/Kong/kong），编程成本相对高一点

Spring Cloud Gateway（取代了 Zuul）性能高、可以用 Java 代码来写逻辑，适于学习

网关技术选型：https://zhuanlan.zhihu.com/p/500587132

### Spring Cloud Gateway 用法

去看官网：https://spring.io/projects/spring-cloud-gateway/

官方文档：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/

### 核心概念

路由（根据什么条件，转发请求到哪里）

断言：一组规则、条件，用来确定如何转发路由

过滤器：对请求进行一系列的处理，比如添加请求头、添加请求参数

请求流程：

- 客户端发起请求
- Handler Mapping：根据断言，去将请求转发到对应的路由
- Web Handler：处理请求（一层层经过过滤器）
- 实际调用服务

![image-20230123234927600](D:\Typora\images\image-20230123234927600.png)

### 两种配置方式

- 配置式（方便、规范）

  - 简化版

  - 全称版

- 编程式（灵活、相对麻烦）

### 建议开启日志

![image-20230123235004871](D:\Typora\images\image-20230123235004871.png)

### 断言

- After 在 xx 时间之后
- Before 在 xx 时间之前
- Between 在 xx 时间之间
- 请求类别
- 请求头（包含 Cookie）
- 查询参数
- 客户端地址
- 权重

### 过滤器

基本功能：对请求头、请求参数、响应头的增删改查

- 添加请求头
- 添加请求参数
- 添加响应头
- 降级
- 限流
- 重试

引入：

![image-20230123235109438](D:\Typora\images\image-20230123235109438.png)

## 第五期

主要内容：

实现统一的接口鉴权和计费（API 网关实践）

### 要用到的特性

- 路由（转发请求到模拟接口项目）
- 统一鉴权（accesskey, secretKey）
- 统一业务处理（每次请求接口后，接口调用次数 +1）
- 访问控制（黑白名单）
- 流量染色（记录请求是否为网关来的）
- 统一日志（记录每次的请求和响应日志）

### 业务逻辑

1. 用户发送请求到 API 网关
2. 请求日志
3. （黑白名单）
4. 用户鉴权（判断 ak、sk 是否合法）
5. 请求的模拟接口是否存在？
6. 请求转发，调用模拟接口
7. 响应日志
8. 调用成功，接口调用次数 + 1
9. 调用失败，返回一个规范的错误码

### 具体实现

使用前缀匹配断言：https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/#the-path-route-predicate-factory

所有路径为：/api/** 的请求进行转发，转发到 http://localhost:8123/api/** 

比如请求网关：http://localhost:8090/api/name/get?name=yupi

转发到：http://localhost:8123/api//name/get?name=antony

http://localhost:8123/api/name/get?name=antony

![image-20230123235400580](D:\Typora\images\image-20230123235400580.png)

### 编写业务逻辑

使用了 GlobalFilter（编程式），全局请求拦截处理（类似 AOP）

因为网关项目没引入 MyBatis 等操作数据库的类库，如果该操作较为复杂，可以由 backend 增删改查项目提供接口，我们直接调用，不用再重复写逻辑了。

- HTTP 请求（用 HTTPClient、用 RestTemplate、Feign）
- RPC（Dubbo）

### 问题

预期是等模拟接口调用完成，才记录响应日志、统计调用次数。

但现实是 chain.filter 方法立刻返回了，直到 filter 过滤器 return 后才调用了模拟接口。

原因是：chain.filter 是个异步操作，理解为前端的 promise

解决方案：利用 response 装饰者，增强原有 response 的处理能力

参考博客：https://blog.csdn.net/qq_19636353/article/details/126759522（以这个为主）

其他参考：

- https://blog.csdn.net/m0_67595943/article/details/124667975
- [https://blog.csdn.net/weixin_43933728/article/details/121359727](https://blog.csdn.net/weixin_43933728/article/details/121359727?spm=1001.2014.3001.5501)
- https://blog.csdn.net/zx156955/article/details/121670681
- https://blog.csdn.net/qq_39529562/article/details/108911983

## 第六期

主要内容：

实现统一的接口鉴权和计费（RPC、Dubbo 讲解）

### 网关业务逻辑

问题：网关项目比较纯净，没有操作数据库的包、并且还要调用我们之前写过的代码？复制粘贴维护麻烦

理想：直接请求到其他项目的方法

### 怎么调用其他项目的方法？

- 复制代码和依赖、环境
- HTTP 请求（提供一个接口，供其他项目调用）
- RPC
- 把公共的代码打个 jar 包，其他项目去引用（客户端 SDK）

### HTTP 请求怎么调用？

- 提供方开发一个接口（地址、请求方法、参数、返回值）
- 调用方使用 HTTP Client 之类的代码包去发送 HTTP 请求

### RPC

作用：像调用本地方法一样调用远程方法。

和直接 HTTP 调用的区别：

- 对开发者更透明，减少了很多的沟通成本。
- RPC 向远程服务器发送请求时，未必要使用 HTTP 协议，比如还可以用 TCP / IP，性能更高。（内部服务更适用）

RPC 调用模型：

![image-20230124002659478](D:\Typora\images\image-20230124002659478.png)

### Dubbo 框架（RPC 实现）

其他 RPC 框架：GRPC、TRPC

最好的学习方式：阅读官方文档

https://dubbo.incubator.apache.org/zh/docs3-v2/java-sdk/quick-start/spring-boot/

两种使用方式：

Spring Boot 代码（注解 + 编程式）：写 Java 接口，服务提供者和消费者都去引用这个接口

IDL（接口调用语言）：创建一个公共的接口定义文件，服务提供者和消费者读取这个文件。优点是跨语言，所有的框架都认识

底层是 Triple 协议：https://dubbo.incubator.apache.org/zh/docs3-v2/java-sdk/concepts-and-architecture/triple/

**示例项目学习**

zookeeper 注册中心：通过内嵌的方式运行，更方便

最先启动注册中心，先启动服务提供者，再启动服务消费者

**整合运用**

- backend 项目作为服务提供者，提供 3 个方法：
  - 实际情况应该是去数据库中查是否已分配给用户
  - 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
  - 调用成功，接口调用次数 + 1 invokeCount
- gateway 项目作为服务调用者，调用这 3 个方法

建议大家用 Nacos！

整合 Nacos 注册中心：https://dubbo.apache.org/zh/docs3-v2/java-sdk/reference-manual/registry/nacos/

注意：

- 服务接口类必须要在同一个包下，建议是抽象出一个公共项目（放接口、实体类等）
- 设置注解（比如启动类的 EnableDubbo、接口实现类和 Bean 引用的注解）
- 添加配置
- 服务调用项目和提供者项目尽量引入相同的依赖和配置

![image-20230124105952742](D:\Typora\images\image-20230124105952742.png)

## 第七期

主要内容：

- 开发抽象公共服务
- 实现网关核心业务流程
- 开发管理员接口分析功能
- 上线分析和扩展

### 梳理网关业务逻辑

以下操作可以复用：

- 实际情况应该是去数据库中查是否已分配给用户秘钥（ak、sk是否合法） 
  - 先根据 accessKey 判断用户是否存在，查到 secretKey
  - 对比 secretKey 和用户传的加密后的 secretKey 是否一致
- 从数据库中查询模拟接口是否存在，以及请求方法是否匹配（还可以校验请求参数）
- 调用成功，接口调用次数 + 1 invokeCount

### 临时问题：如何获取接口转发服务器的地址

思路：网关启动时，获取所有的接口信息，维护到内存的 hashmap 中；有请求时，根据请求的 url 路径或者其他参数（比如 host 请求头）来判断应该转发到哪台服务器、以及用于校验接口是否存在

### 抽象公共服务

项目名：proapi-common

目的是让方法、实体类在多个项目间复用，减少重复编写。

服务抽取：

- 数据库中查是否已分配给用户秘钥（根据 accessKey 拿到用户信息，返回用户信息，为空表示不存在）
- 从数据库中查询模拟接口是否存在（请求路径、请求方法、请求参数，返回接口信息，为空表示不存在）
- 接口调用次数 + 1 invokeCount（accessKey、secretKey（标识用户），请求接口路径）

步骤：

- 新建干净的 maven 项目，只保留必要的公共依赖
- 抽取 service 和实体类
- install 本地 maven 包
- 让服务提供者引入 common 包，测试是否正常运行
- 让服务消费者引入 common 包

### 统计分析功能

**需求**

各接口的总调用次数占比（饼图）取调用最多的前 3 个接口，从而分析出哪些接口没有人用（降低资源、或者下线），高频接口（增加资源、提高收费）。

用饼图展示。

**实现**

强烈推荐用现成的库！！！

比如：

- ECharts：https://echarts.apache.org/zh/index.html（推荐）
- AntV：https://antv.vision/zh（推荐）
- BizCharts

用法贼简单！

- 看官网
- 找到快速入门、按文档去引入库
- 进入示例页面
- 找到你要的图
- 在线调试
- 复制代码
- 改为真实数据

如果是 React 项目，用这个库：https://github.com/hustcc/echarts-for-react

**后端**

写一个接口，得到下列示例数据：

接口 A：2次

接口 B：3次

步骤：

- SQL 查询调用数据：select interfaceInfoId, sum(totalNum) as totalNum from user_interface_info group by interfaceInfoId order by totalNum desc limit 3;
- 业务层去关联查询接口信息

**上线计划**

前端：参考之前用户中心或伙伴匹配系统的上线方式

后端：

- backend 项目：web 项目，部署 spring boot 的 jar 包（对外的）
- gateway 网关项目：web 项目，部署 spring boot 的 jar 包（对外的）
- interface 模拟接口项目：web 项目，部署 spring boot 的 jar 包（不建议对外暴露的）

**关键：网络必须要连通**

如果自己学习用：单个服务器部署这三个项目就足够。

如果你是搞大事，多个服务器建议在 同一内网 ，内网交互会更快、且更安全。

### 扩展思路

**用户可以申请更换签名**

**怎么让其他用户也上传接口？**

需要提供一个机制（界面），让用户输入自己的接口 host（服务器地址）、接口信息，将接口信息写入数据库。

可以在 interfaceInfo 表里加个 host 字段，区分服务器地址，让接口提供者更灵活地接入系统。

将接口信息写入数据库之前，要对接口进行校验（比如检查他的地址是否遵循规则，测试调用），保证他是正常的。

将接口信息写入数据库之前遵循咱们的要求（并且使用咱们的 sdk），在接入时，平台需要测试调用这个接口，保证他是正常的。

**网关校验是否还有调用次数**

需要考虑并发问题，防止瞬间调用超额。

**网关优化**

比如增加限流 / 降级保护，提高性能等。还可以考虑搭配 Nginx 网关使用。

**功能增强**

可以针对不同的请求头或者接口类型来设计前端界面和表单，便于用户调用，获得更好的体验。
可以参考 swagger、postman、knife4j 的页面。

# 技术栈

项目名称：XX API 开放平台（或者 XX 接口平台、XX 免费接口平台、XX API 等）

在线访问：xxx（建议提供可访问的、简短的线上地址）

项目介绍：

基于 React + Spring Boot + Dubbo + Gateway 的 API 接口开放调用平台。管理员可以接入并发布接口，可视化各接口调用情况；用户可以开通接口调用权限、浏览接口及在线调试，并通过客户端 SDK 轻松调用接口。

主要工作（根据自己的方向选 6 个左右去写并适当调整文案，如果自己没有实现或不理解，就先不要写，灵活一点）：

1. 根据业务流程，将整个项目后端划分为 web 系统、模拟接口、公共模块、客户端 SDK、API 网关这 5 个子项目，并使用 Maven 进行多模块依赖管理和打包。
2. 使用 Ant Design Pro 脚手架 + 自建 Spring Boot 项目模板快速构建初始 web 项目，并实现了前后端统一权限管理、多环境切换等基础能力。
3. 基于 MyBatis Plus 框架的 QueryWrapper 实现对 MySQL 数据库的灵活查询，并配合 MyBatis X 插件自动生成后端 CRUD 基础代码，减少重复工作。
4. 前端：后端使用 Swagger + Knife4j 自动生成 OpenAPI 规范的接口文档，前端在此基础上使用插件自动生成接口请求代码，降低前后端协作成本。
5. 为防止接口被恶意调用，设计 API 签名认证算法，为用户分配唯一 ak / sk 以鉴权，保障调用的安全性、可溯源性（指便于统计接口调用次数）。
6. 为解决开发者调用成本过高的问题（须自己使用 HTTP + 封装签名去调用接口，平均 20 行左右代码），基于 Spring Boot Starter 开发了客户端 SDK，一行代码 即可调用接口，提高开发体验。
7. 前端：使用 ECharts（或 AntV）可视化库实现了接口调用的分析图表（如饼图），并通过 loading 配置提高加载体验。
8. 选用 Spring Cloud Gateway 作为 API 网关，实现了路由转发、访问控制、流量染色，并集中处理签名校验、请求参数校验、接口调用统计等业务逻辑，提高安全性的同时、便于系统开发维护。（更多 API 网关的优点参考：https://blog.csdn.net/guorui_java/article/details/124112897）
9. 为解决多个子系统内代码大量重复的问题，抽象模型层和业务层代码为公共模块，并使用 Dubbo RPC 框架实现子系统间的高性能接口调用（实测单机 qps 达 xx），大幅减少重复代码。

扩展思路：

- 前端：参考 Swagger（或 Postman）等 API 管理产品实现了 API 文档浏览及在线调用功能，并提供多级联动表单来提升用户输入请求参数 json 的体验。
- 前端可以阅读 Ant Design Pro 的文档，从框架的特性出发、结合自己做的功能去写一些亮点。
- 如果项目已上线且提供了一些 API 接口，可以说：自主开发了 XX、XX 等 API 接口并接入系统，累积调用次数达 xx 次，且接口调用可用性达 99.999%（4 - 5 个 9 都可以）
- 客户端 SDK 尽量使用最少的依赖，可以补充提一些 SDK 设计的亮点，比如：基于 Spring Boot Starter，自主设计客户端 SDK，并遵循 xx、xx、xx 等规范，保证了 SDK 的精简、避免依赖冲突。
- 使用 Docker 或 Docker Compose 来部署项目，可以写：自主编写 Dockerfile，并通过第三方容器托管平台实现自动化镜像构建及容器部署，提高部署上线效率。
- 思考如何保证接口调用的性能、稳定性和可用性（比如在网关增加限流 / 降级保护）？
- 思考如何提高开发者接入平台的效率、安全性等，真正地让平台成为开放平台。
- 可以考虑在微服务网关前搭建 Nginx 网关，通过负载均衡实现更大的并发。
- 在数据量大的情况下，使用 Spring Scheduler 定时任务离线计算结果集来替代实时查询，提高了后端统计分析的性能，单次查询响应时长从 xx 降低至 xx。

如果简历内容过少，可以补充【个人介绍】或【自我评价】板块，提到：有较强的文档阅读能力，曾自主阅读 Spring Cloud Gateway、Dubbo（Ant Design Pro）等官方文档并能够运用到项目中。
